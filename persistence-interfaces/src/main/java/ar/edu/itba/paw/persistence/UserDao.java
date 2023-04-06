package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;

import java.sql.SQLException;

public interface UserDao extends RWDao<Long,User> {
    User create(final String email, final String password, final String name) throws SQLException;
}
