package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Roles;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.ImageDao;
import ar.edu.itba.paw.persistence.RecoveryDao;
import ar.edu.itba.paw.persistence.UserDao;
import ar.edu.itba.paw.persistence.exceptions.UserEmailAlreadyTakenPersistenceException;
import ar.edu.itba.paw.services.exceptions.InvalidTokenException;
import ar.edu.itba.paw.services.exceptions.OldPasswordDoesNotMatchException;
import ar.edu.itba.paw.services.exceptions.UserEmailAlreadyTakenException;
import ar.edu.itba.paw.services.exceptions.UserEmailNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final RecoveryDao recDao;
    private final ImageDao imageDao;

    private final MailService mailService;

    private final PasswordEncoder passwordEncoder;

    private static final int MAX_IMAGE_SIZE = 1024 * 1024 * 5;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserDao userDao, RecoveryDao recDao, ImageDao imageDao, MailService mailService, final PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.recDao = recDao;
        this.imageDao = imageDao;
        this.mailService = mailService;
    }


    @Override
    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Transactional
    @Override
    public User create(User.UserBuilder userBuilder, String baseUrl, byte[] profilePic) throws UserEmailAlreadyTakenException {
        long imageId = imageDao.insertAndReturnKey(profilePic);
        userBuilder.imageId(imageId);
        userBuilder.password(passwordEncoder.encode(userBuilder.getPassword()));

        final SecureRandom random = new SecureRandom();
        final byte[] bytes = new byte[20];
        random.nextBytes(bytes);

        final String confirmToken = new String(Base64.getUrlEncoder().encode(bytes));

        User user;
        try {
            user = userDao.create(userBuilder, confirmToken);
        } catch (final UserEmailAlreadyTakenPersistenceException e) {
            LOGGER.warn("User {} failed to create", userBuilder.getEmail());
            throw new UserEmailAlreadyTakenException();
        }

        Map<String,Object> mailModel = new HashMap<>();
        mailModel.put("logoUrl", baseUrl + "/img/uni.png");
        mailModel.put("url", baseUrl + "/confirm/" + confirmToken);
        mailService.sendMail(userBuilder.getEmail(), "Email confirmation", "confirmation", mailModel);

        return user;
    }

    @Transactional
    @Override
    public User create(User.UserBuilder userBuilder, String baseUrl) throws UserEmailAlreadyTakenException, IOException {
        File file = ResourceUtils.getFile("classpath:images/default_user.png");
        byte[] defaultImg = Files.readAllBytes(file.toPath());
        return create(userBuilder, baseUrl, defaultImg);
    }

    @Transactional
    @Override
    public void updateProfilePicture(User user, byte[] image){
        if(image.length > MAX_IMAGE_SIZE){
            return;
        }

        Image newImage = new Image(user.getImageId(), image);
        imageDao.update(newImage);
    }

    @Override
    public Optional<User> getUserWithEmail(String email) {
        return userDao.getUserWithEmail(email);
    }

    @Override
    public Integer getUserSubjectProgress(Long id, String idSub) {
        return userDao.getUserSubjectProgress(id,idSub).orElse(0);
    }


    @Override
    public Map<String, Integer> getUserAllSubjectProgress(Long id) {
        return userDao.getUserAllSubjectProgress(id);
    }

    @Transactional
    @Override
    public Integer deleteUserProgressForSubject(Long id, String idSub){
        return userDao.deleteUserProgressForSubject(id,idSub);
    }

    @Transactional
    @Override
    public Integer updateSubjectProgress(Long id, String idSub, Integer newProgress) {
        return userDao.updateSubjectProgress(id,idSub,newProgress);
    }

    @Transactional
    @Override
    public void changePassword(Long userId, String password, String oldPassword, String userOldPassword) throws OldPasswordDoesNotMatchException {

        if(!passwordEncoder.matches(oldPassword, userOldPassword)){
            LOGGER.warn("Old password does not match with input. Update failed");
            throw new OldPasswordDoesNotMatchException();
        }
        userDao.changePassword(userId, passwordEncoder.encode(password));
    }

    @Transactional
    @Override
    public void editProfile(Long userId, String username) {
        userDao.editProfile(userId, username);
    }

    @Override
    public void sendRecoveryMail(String email, String baseUrl){
        final Optional<User> optUser = getUserWithEmail(email);
        if(!optUser.isPresent()){
            LOGGER.warn("Generation of recovery token failed. User not found");
            throw new UserEmailNotFoundException();
        }
        final User user = optUser.get();


        final SecureRandom random = new SecureRandom();
        final byte[] bytes = new byte[20];
        random.nextBytes(bytes);

        final String token = new String(Base64.getUrlEncoder().encode(bytes));

        recDao.create(token, user.getId());

        Map<String,Object> mailModel = new HashMap<>();
        mailModel.put("logoUrl", baseUrl + "/img/uni.png");
        mailModel.put("url", baseUrl + "/recover/" + token);
        mailService.sendMail(user.getEmail(), "Uni: Recover password", "recovery", mailModel);
    }

    @Override
    public boolean isValidRecoveryToken(String token) {
        return recDao.findUserIdByToken(token).isPresent();
    }

    @Transactional
    @Override
    public void recoverPassword(String token, String newPassword) throws InvalidTokenException {
        Optional<Long> optUserId = recDao.findUserIdByToken(token);
        if(!optUserId.isPresent()){
            LOGGER.info("Invalid token when trying to recover password");
            throw new InvalidTokenException();
        }
        long userId = optUserId.get();

        userDao.changePassword(userId, passwordEncoder.encode(newPassword));
        recDao.delete(token);
    }

    @Override
    public User confirmUser(String token) throws InvalidTokenException {
        Optional<User> optUser = userDao.findUserByConfirmToken(token);
        if(!optUser.isPresent()){
            LOGGER.info("Invalid token when trying to confirm user");
            throw new InvalidTokenException();
        }
        User user = optUser.get();

        userDao.confirmUser(user.getId());

        return user;
    }

    //-------------------------------- USER ROLES -----------------------------
    @Override
    public List<Roles> getUserRoles(Long userId) {
        return userDao.getUserRoles(userId);
    }

    @Override
    public Integer addIdToUserRoles(Long roleId, Long userId) {
        return userDao.addIdToUserRoles(roleId, userId);
    }

    public Integer updateUserRoles(Long roleId, Long userId) {
        return userDao.updateUserRoles(roleId, userId);
    }
}
