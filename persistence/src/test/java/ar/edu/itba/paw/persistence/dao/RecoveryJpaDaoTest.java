package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.RecoveryToken;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.mock.RecoveryMockData;
import ar.edu.itba.paw.persistence.mock.UserMockData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class RecoveryJpaDaoTest {
    @Autowired
    private DataSource dataSource;
    @PersistenceContext
    private EntityManager em;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RecoveryJpaDao recoveryJpaDao;


    @Before
    public void setup() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    public void create() {
        final String tokenToCreate = "48845";
        final User user = em.find(User.class, UserMockData.USER1_ID);

        final RecoveryToken recoveryToken = recoveryJpaDao.create(tokenToCreate, user);
        em.flush();

        assertEquals(tokenToCreate, recoveryToken.getToken());
        assertEquals(user, recoveryToken.getUser());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate,
                "recoverytoken",
                "token = '" + tokenToCreate + "' AND userid = " + UserMockData.USER1_ID
                ));
    }

    @Test
    public void findUserByToken() {
        final User expectedUser = em.find(User.class, UserMockData.USER2_ID);

        final Optional<User> user = recoveryJpaDao.findUserByToken(RecoveryMockData.TOKEN);

        assertTrue(user.isPresent());
        assertEquals(expectedUser, user.get());
    }

    @Test
    public void delete() {
        final RecoveryToken tokenToDelete = em.find(RecoveryToken.class, RecoveryMockData.TOKEN);

        recoveryJpaDao.delete(tokenToDelete);
        em.flush();

        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate,"recoverytoken"));
    }
}
