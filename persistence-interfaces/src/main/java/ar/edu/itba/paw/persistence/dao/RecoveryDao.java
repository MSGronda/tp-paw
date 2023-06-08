package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.RecoveryToken;
import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface RecoveryDao {
    void create(final String token, final User user);
    Optional<User> findUserByToken(final String token);
    void delete(final RecoveryToken token);
}
