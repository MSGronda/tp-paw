package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.RecoveryToken;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class RecoveryJpaDao implements RecoveryDao {
    private final static Logger LOGGER = LoggerFactory.getLogger(RecoveryJpaDao.class);
    @PersistenceContext
    private EntityManager em;

    @Override
    public RecoveryToken create(final String token, final User user) {
        final RecoveryToken recToken = new RecoveryToken(token, user);
        em.persist(recToken);
        LOGGER.info("Created recovery token for user with id: {}", user.getId());
        return recToken;
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
        final long userId = token.getUser().getId();
        em.remove(token);
        LOGGER.info("Deleted recovery token for user with id: {}", userId);
    }
}
