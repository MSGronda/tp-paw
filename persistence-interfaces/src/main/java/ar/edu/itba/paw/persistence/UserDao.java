package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.sql.SQLException;
import java.util.Optional;

public interface UserDao extends RWDao<Long,User> {
    User create(final String email, final String password, final String name) throws SQLException;

    Optional<User> getUserWithEmail(final String email);
}
