package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.RecoveryToken;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class RecoveryJpaDaoTest {
    private final static String RECOVERY_TOKEN = "rec_token";

    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";
    private final static String EMAIL = "e@mail.com";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private RecoveryJpaDao recoveryJpaDao;

    @Before
    public void clear() {
        em.createQuery("DELETE FROM RecoveryToken").executeUpdate();
        em.createQuery("DELETE FROM User").executeUpdate();
        em.createQuery("DELETE FROM Role").executeUpdate();
    }

    @Test
    public void create() {
        final User user = User.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .email(EMAIL)
                .build();

        em.persist(user);

        recoveryJpaDao.create(RECOVERY_TOKEN, user);

        assertEquals(user, em.find(RecoveryToken.class, RECOVERY_TOKEN).getUser());
    }

    @Test
    public void findUserByToken() {
        final User user = User.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .email(EMAIL)
                .build();

        em.persist(user);

        recoveryJpaDao.create(RECOVERY_TOKEN, user);

        assertEquals(user, recoveryJpaDao.findUserByToken(RECOVERY_TOKEN).get());
    }

    @Test
    public void delete() {
        final User user = User.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .email(EMAIL)
                .build();

        em.persist(user);

        final RecoveryToken recoveryToken = new RecoveryToken(RECOVERY_TOKEN, user);
        em.persist(recoveryToken);

        recoveryJpaDao.delete(recoveryToken);
        assertNull(em.find(RecoveryToken.class, RECOVERY_TOKEN));
    }
}
