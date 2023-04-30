package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface RecoveryDao {
    void create(String token, long userId);
    Optional<Long> findUserIdByToken(String token);
    void delete(String token);
    void delete(long userId);
}
