package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.RecoveryToken;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.dao.RecoveryDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class RecoveryJpaDao implements RecoveryDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(final String token, final User user) {
        final RecoveryToken recToken = new RecoveryToken(token, user);
        em.persist(recToken);
    }

    @Override
    public Optional<User> findUserByToken(final String token) {
        return Optional.ofNullable(em.find(RecoveryToken.class, token))
                .map(RecoveryToken::getUser);
    }

    @Override
    public void delete(final RecoveryToken token) {
        if(token == null) {
            throw new IllegalArgumentException("Token can't be null");
        }

        em.remove(token);
    }
}
