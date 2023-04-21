package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;

import java.sql.SQLException;
import java.util.Optional;

public interface UserService extends BaseService<Long, User> {
    User create(String email, String password,String username) throws SQLException;

    Optional<User> getUserWithEmail(String email);
}
