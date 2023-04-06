package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;

import java.sql.SQLException;

public interface UserService extends BaseService<Long, User> {
    User create(String email, String password, String username) throws SQLException;
}
