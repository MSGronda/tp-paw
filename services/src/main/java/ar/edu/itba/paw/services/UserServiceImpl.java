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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.security.Security;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final RecoveryDao recDao;
    private final ImageDao imageDao;

    private final MailService mailService;
    private final RolesService rolesService;

    private final PasswordEncoder passwordEncoder;

    private static final int MAX_IMAGE_SIZE = 1024 * 1024 * 5;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(final UserDao userDao, final RecoveryDao recDao, final ImageDao imageDao, final MailService mailService,
                           final RolesService rolesService, final PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.recDao = recDao;
        this.imageDao = imageDao;
        this.mailService = mailService;
        this.rolesService = rolesService;
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
    public User create(final User.UserBuilder userBuilder, final byte[] profilePic) throws UserEmailAlreadyTakenException {
        long imageId = imageDao.insertAndReturnKey(profilePic);

        final SecureRandom random = new SecureRandom();
        final byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        final String confirmToken = new String(Base64.getUrlEncoder().encode(bytes));

        userBuilder.imageId(imageId)
            .password(passwordEncoder.encode(userBuilder.getPassword()))
            .confirmToken(confirmToken);

        User user;
        try {
            user = userDao.create(userBuilder);
        } catch (final UserEmailAlreadyTakenPersistenceException e) {
            LOGGER.info("User {} failed to create", userBuilder.getEmail());
            throw new UserEmailAlreadyTakenException();
        }

        Optional<Roles> maybeRole = rolesService.findByName("USER");
        if(!maybeRole.isPresent()){
            throw new IllegalStateException("USER role not found");
        }
        Roles role = maybeRole.get();
        addIdToUserRoles(role.getId(), user.getId());


        return user;
    }

    @Transactional
    @Override
    public User create(final User.UserBuilder userBuilder) throws UserEmailAlreadyTakenException, IOException {
        File file = ResourceUtils.getFile("classpath:images/default_user.png");
        byte[] defaultImg = Files.readAllBytes(file.toPath());
        return create(userBuilder, defaultImg);
    }

    @Transactional
    @Override
    public void updateProfilePicture(final User user, final byte[] image){
        if(image.length > MAX_IMAGE_SIZE){
            return;
        }

        Image newImage = new Image(user.getImageId(), image);
        imageDao.update(newImage);
    }

    @Override
    public Optional<User> getUserWithEmail(final String email) {
        return userDao.getUserWithEmail(email);
    }

    @Override
    public Integer getUserSubjectProgress(final Long id, final String idSub) {
        return userDao.getUserSubjectProgress(id,idSub).orElse(0);
    }


    @Override
    public Map<String, Integer> getUserAllSubjectProgress(final Long id) {
        return userDao.getUserAllSubjectProgress(id);
    }

    @Transactional
    @Override
    public Integer deleteUserProgressForSubject(final Long id, final String idSub){
        return userDao.deleteUserProgressForSubject(id,idSub);
    }

    @Transactional
    @Override
    public Integer updateSubjectProgress(final Long id, final String idSub, final Integer newProgress) {
        return userDao.updateSubjectProgress(id,idSub,newProgress);
    }

    @Transactional
    @Override
    public void changePassword(final Long userId, final String password, final String oldPassword,
                               final String userOldPassword) throws OldPasswordDoesNotMatchException {

        if(!passwordEncoder.matches(oldPassword, userOldPassword)){
            LOGGER.warn("Old password does not match with input. Update failed");
            throw new OldPasswordDoesNotMatchException();
        }
        userDao.changePassword(userId, passwordEncoder.encode(password));
    }

    @Transactional
    @Override
    public void editProfile(final Long userId, final String username) {
        userDao.editProfile(userId, username);
    }

    @Override
    public String sendRecoveryMail(final String email){
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

        return token;
    }

    @Override
    public boolean isValidRecoveryToken(final String token) {
        return recDao.findUserIdByToken(token).isPresent();
    }

    @Transactional
    @Override
    public void recoverPassword(final String token, final String newPassword) throws InvalidTokenException {
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
    public void confirmUser(final String token) throws InvalidTokenException {
        Optional<User> optUser = userDao.findUserByConfirmToken(token);
        if(!optUser.isPresent()){
            LOGGER.info("Invalid token when trying to confirm user");
            throw new InvalidTokenException();
        }
        User user = optUser.get();

        userDao.confirmUser(user.getId());
        autoLogin(user.getId());
    }

    private void autoLogin(final long userId) {
        final Optional<User> maybeUser = findById(userId);
        if(!maybeUser.isPresent()){
            LOGGER.info("No registered user with id {}", userId);
            throw new UserEmailNotFoundException();
        }
        User user = maybeUser.get();
        final Collection<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        LOGGER.info("Auto login for user {}", userId);
    }

    //-------------------------------- USER ROLES -----------------------------
    @Override
    public List<Roles> getUserRoles(final Long userId) {
        return userDao.getUserRoles(userId);
    }

    @Override
    public Integer addIdToUserRoles(final Long roleId, final Long userId) {
        return userDao.addIdToUserRoles(roleId, userId);
    }

    public Integer updateUserRoles(final Long roleId, final Long userId) {
        return userDao.updateUserRoles(roleId, userId);
    }
}
