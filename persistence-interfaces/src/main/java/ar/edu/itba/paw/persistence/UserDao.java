package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserDao extends RWDao<Long,User> {
    User create(User.UserBuilder userBuilder) throws SQLException;

    Optional<User> findByIdWithImage(Long id);

    List<User> getAllWithImage();

    Optional<User> getUserWithEmail(final String email);

    void changePassword(String email, String password);
}
