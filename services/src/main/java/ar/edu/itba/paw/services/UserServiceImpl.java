package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.UserDao;
import ar.edu.itba.paw.persistence.exceptions.UserEmailAlreadyTakenPersistenceException;
import ar.edu.itba.paw.services.exceptions.UserEmailAlreadyTakenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    private final PasswordEncoder passwordEncoder;

    private static final int MAX_IMAGE_SIZE = 1024 * 1024 * 5;

    @Autowired
    public UserServiceImpl(UserDao userDao, final PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
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
    public Optional<Integer> getUserSubjectProgress(Long id, String idSub) {
        return userDao.getUserSubjectProgress(id,idSub);
    }


    @Override
    public Map<String, Integer> getUserAllSubjectProgress(Long id) {
        return userDao.getUserAllSubjectProgress(id);
    }

    @Override
    public void updateSubjectProgress(Long id, String idSub, Integer newProgress) {
        userDao.updateSubjectProgress(id,idSub,newProgress);
    }

    @Override
    public void changePassword(Long userId, String password) {
        userDao.changePassword(userId, passwordEncoder.encode(password));
    }

    @Override
    public void editProfile(Long userId, String username) {
        userDao.editProfile(userId, username);
    }
    
}
