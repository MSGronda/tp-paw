package ar.edu.itba.paw.persistence.dao;

import java.util.Optional;

public interface RecoveryDao {
    void create(final String token, final long userId);
    Optional<Long> findUserIdByToken(final String token);
    void delete(final String token);
    void delete(final long userId);
}
