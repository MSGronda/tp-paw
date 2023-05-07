package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Roles;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.RecoveryDao;
import ar.edu.itba.paw.persistence.UserDao;
import ar.edu.itba.paw.persistence.exceptions.UserEmailAlreadyTakenPersistenceException;
import ar.edu.itba.paw.services.exceptions.InvalidTokenException;
import ar.edu.itba.paw.services.exceptions.OldPasswordDoesNotMatchException;
import ar.edu.itba.paw.services.exceptions.UserEmailAlreadyTakenException;
import ar.edu.itba.paw.services.exceptions.UserEmailNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final RecoveryDao recDao;

    private final PasswordEncoder passwordEncoder;

    private static final int MAX_IMAGE_SIZE = 1024 * 1024 * 5;

    @Autowired
    public UserServiceImpl(UserDao userDao, RecoveryDao recDao, final PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.recDao = recDao;
    }


    @Override
    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public User create(User.UserBuilder userBuilder) throws UserEmailAlreadyTakenException {

        userBuilder.password(passwordEncoder.encode(userBuilder.getPassword()));
        try {
            return userDao.create(userBuilder);
        } catch (final UserEmailAlreadyTakenPersistenceException e) {
            throw new UserEmailAlreadyTakenException();
        }

//        return userDao.create(new User.UserBuilder(userBuilder.getEmail(), passwordEncoder.encode(userBuilder.getPassword()), userBuilder.getUsername()));
    }

    @Override
    public byte[] updateProfilePicture(long id, byte[] image){
        if(image.length > MAX_IMAGE_SIZE){
            return null;
        }
        return userDao.updateProfilePicture(id,image);
    }

    @Override
    public Optional<User> getUserWithEmail(String email) {
        return userDao.getUserWithEmail(email);
    }
    @Override
    public List<User> getAllWithImage() {
        return userDao.getAllWithImage();
    }

    @Override
    public Optional<User> findByIdWithImage(Long id) {
        return userDao.findByIdWithImage(id);
    }

    @Override
    public Integer getUserSubjectProgress(Long id, String idSub) {
        return userDao.getUserSubjectProgress(id,idSub).orElse(0);
    }


    @Override
    public Map<String, Integer> getUserAllSubjectProgress(Long id) {
        return userDao.getUserAllSubjectProgress(id);
    }

    @Override
    public Integer deleteUserProgressForSubject(Long id, String idSub){
        return userDao.deleteUserProgressForSubject(id,idSub);
    }

    @Override
    public Integer updateSubjectProgress(Long id, String idSub, Integer newProgress) {
        return userDao.updateSubjectProgress(id,idSub,newProgress);
    }

    @Override
    public void changePassword(Long userId, String password, String oldPassword, String userOldPassword) throws OldPasswordDoesNotMatchException {

        if(!passwordEncoder.matches(oldPassword, userOldPassword)){
            throw new OldPasswordDoesNotMatchException();
        }
        userDao.changePassword(userId, passwordEncoder.encode(password));
    }

    @Override
    public void editProfile(Long userId, String username) {
        userDao.editProfile(userId, username);
    }

    @Override
    public String generateRecoveryToken(String email){
        Optional<User> user = getUserWithEmail(email);
        if(!user.isPresent()){
            throw new UserEmailNotFoundException();
        }
        return generateRecoveryToken(user.get());
    }

    @Override
    public String generateRecoveryToken(User user) {
        return generateRecoveryToken(user.getId());
    }

    @Override
    public String generateRecoveryToken(long userId) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);

        final String token = new String(Base64.getUrlEncoder().encode(bytes));

        recDao.create(token, userId);

        return token;
    }

    @Override
    public boolean isValidToken(String token) {
        return recDao.findUserIdByToken(token).isPresent();
    }

    @Override
    public void recoverPassword(String token, String newPassword) throws InvalidTokenException {
        Optional<Long> optUserId = recDao.findUserIdByToken(token);
        if(!optUserId.isPresent()){
            throw new InvalidTokenException();
        }
        long userId = optUserId.get();

        userDao.changePassword(userId, passwordEncoder.encode(newPassword));
        recDao.delete(token);
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
