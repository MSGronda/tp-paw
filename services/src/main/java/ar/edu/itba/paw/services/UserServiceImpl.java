package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    private final PasswordEncoder passwordEncoder;

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
    public User create(User.UserBuilder userBuilder) throws SQLException {

        userBuilder.password(passwordEncoder.encode(userBuilder.getPassword()));
        return userDao.create(userBuilder);

//        return userDao.create(new User.UserBuilder(userBuilder.getEmail(), passwordEncoder.encode(userBuilder.getPassword()), userBuilder.getUsername()));
    }

    @Override
    public Optional<User> getUserWithEmail(String email) {
        return userDao.getUserWithEmail(email);
    }

    @Override
    public void changePassword(String email, String password) {
        userDao.changePassword(email, passwordEncoder.encode(password));
    }


}
