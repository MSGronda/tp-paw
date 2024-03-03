package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.SubjectProgress;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.mock.DegreeMockData;
import ar.edu.itba.paw.persistence.mock.SubjectMockData;
import ar.edu.itba.paw.persistence.mock.UserMockData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
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
public class UserJpaDaoTest {

    @Autowired
    private DataSource dataSource;
    @PersistenceContext
    private EntityManager em;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserJpaDao userJpaDao;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("ALTER SEQUENCE users_id_seq RESTART WITH 3;");
    }

    @Rollback
    @Test
    public void testFindById() {
        final Optional<User> user = userJpaDao.findById(UserMockData.USER1_ID);

        assertTrue(user.isPresent());
        assertEquals(UserMockData.USER1_ID, user.get().getId());
        assertEquals(UserMockData.USER1_USERNAME, user.get().getUsername());
        assertEquals(UserMockData.USER1_EMAIL, user.get().getEmail());
        assertEquals(UserMockData.USER1_PASSWORD, user.get().getPassword());
        assertEquals(UserMockData.USER1_DEGREEID, user.get().getDegree().getId());
    }

    @Rollback
    @Test
    public void testFindByEmail() {
        final Optional<User> user = userJpaDao.findByEmail(UserMockData.USER1_EMAIL);

        assertTrue(user.isPresent());
        assertEquals(UserMockData.USER1_ID, user.get().getId());
        assertEquals(UserMockData.USER1_USERNAME, user.get().getUsername());
        assertEquals(UserMockData.USER1_EMAIL, user.get().getEmail());
        assertEquals(UserMockData.USER1_PASSWORD, user.get().getPassword());
        assertEquals(UserMockData.USER1_DEGREEID, user.get().getDegree().getId());
    }

    @Rollback
    @Test
    public void testFindByToken() {
        final Optional<User> user = userJpaDao.findByConfirmToken(UserMockData.USER1_TOKEN);

        assertTrue(user.isPresent());
        assertEquals(UserMockData.USER1_ID, user.get().getId());
        assertEquals(UserMockData.USER1_USERNAME, user.get().getUsername());
        assertEquals(UserMockData.USER1_EMAIL, user.get().getEmail());
        assertEquals(UserMockData.USER1_PASSWORD, user.get().getPassword());
        assertEquals(UserMockData.USER1_DEGREEID, user.get().getDegree().getId());
    }

    @Rollback
    @Test
    public void testCreateUser() {
        final User.Builder userToCreate = User.builder().email("fake@mail.com").username("New User").password("password").imageId(1).verificationToken("asdf").degree(DegreeMockData.getDegree1());

        final User createdUser = userJpaDao.create(userToCreate);
        em.flush();

        assertEquals(userToCreate.getEmail(), createdUser.getEmail());
        assertEquals(userToCreate.getUsername(), createdUser.getUsername());
        assertEquals(userToCreate.getPassword(), createdUser.getPassword());
        assertEquals(userToCreate.getImageId(), createdUser.getImageId());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "id = " + createdUser.getId()));
    }

    @Rollback
    @Test
    public void testConfirmUser() {
        final User user = em.find(User.class, UserMockData.USER2_ID);

        userJpaDao.confirmUser(user);
        em.flush();

        assertTrue(user.isVerified());
    }

    @Rollback
    @Test
    public void testUpdateUserProgress() {
        final User user = em.find(User.class, UserMockData.USER1_ID);
        final Subject subject = em.find(Subject.class, SubjectMockData.SUB1_ID);

        userJpaDao.updateSubjectProgress(user, subject, SubjectProgress.DONE);
        em.flush();

        assertTrue(user.getAllSubjectProgress().containsKey(SubjectMockData.SUB1_ID));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate,
                "usersubjectprogress",
                "iduser = " + user.getId() + " AND idsub = '" + subject.getId() + "' AND subjectstate = " + SubjectProgress.DONE.getValue()
        ));
    }

    @Rollback
    @Test
    public void testAddRole() {
        final User user = em.find(User.class, UserMockData.USER1_ID);

        userJpaDao.addRole(user, Role.RoleEnum.EDITOR.getRole());
        em.flush();

        assertTrue(user.getRoles().contains(Role.RoleEnum.EDITOR.getRole()));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate,
                "userroles",
                "roleid = " + Role.RoleEnum.EDITOR.getId() + " AND userid = " + user.getId()
        ));
    }

    @Rollback
    @Test
    public void testAddToCurrentSemester() {
        final User user = em.find(User.class, UserMockData.USER1_ID);
        final Subject subject = em.find(Subject.class, SubjectMockData.SUB2_ID);

        userJpaDao.addToCurrentSemester(user, subject.getClassById(SubjectMockData.SUB2_CLASS1_ID).get());
        em.flush();

        assertEquals(1, user.getUserSemester().size());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "class", "idsub = '" + SubjectMockData.SUB2_ID + "'"));
    }

    @Rollback
    @Test
    public void testChangeUsername() {
        final String newName = "New Username";
        final User user = em.find(User.class, UserMockData.USER1_ID);

        userJpaDao.changeUsername(user, newName);

        assertEquals(newName, user.getUsername());

    }
}
