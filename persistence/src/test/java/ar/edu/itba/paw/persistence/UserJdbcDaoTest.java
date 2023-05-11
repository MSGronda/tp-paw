package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.exceptions.UserEmailAlreadyTakenPersistenceException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserJdbcDaoTest {
    private static final long ID = 1;
    private static final String EMAIL = "email";
    private static final String PASSWORD = "pass";

    private static final String NEW_PASSWORD = "newPass";
    private static final String USERNAME = "username";

    private static final String NEW_USERNAME = "new_username";

    private static final long IMAGEID = 1;

    private static final byte[] IMAGE = null;

    private static final int USERID = 3;
    private static final String SUBJECTID = "72.33";
    private static final int SUBJECTPROGRESS = 0;
    private static final int NEWSUBJECTPROGRESS = 1;



    private JdbcTemplate jdbcTemplate;

    private JdbcTemplate jdbcTemplateSubjectP;

    private JdbcTemplate jdbcTemplateImage;

    @Autowired
    private DataSource ds;
    @Autowired
    private UserJdbcDao userDao;

    @Before
    public void setup() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcTemplateSubjectP = new JdbcTemplate(ds);
        jdbcTemplateImage = new JdbcTemplate(ds);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
        JdbcTestUtils.deleteFromTables(jdbcTemplateSubjectP, "usersubjectprogress");
        JdbcTestUtils.deleteFromTables(jdbcTemplateImage, "images");
    }

    @Test
    public void testFindById() {
        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");

        Optional<User> maybeUser = userDao.findById(ID);

        Assert.assertTrue(maybeUser.isPresent());
        Assert.assertEquals(ID, maybeUser.get().getId());
    }

    @Test
    public void testFindByIdDoesNotExist() {
        Optional<User> maybeUser = userDao.findById(ID);

        Assert.assertFalse(maybeUser.isPresent());
    }

    @Test
    public void testCreate() {
        jdbcTemplateImage.execute("INSERT INTO images VALUES (" + IMAGEID + ", " + IMAGE + ")");

        User user;
        try{
            user = userDao.create(new User.UserBuilder(EMAIL, PASSWORD, USERNAME).imageId(IMAGEID));
        }catch (UserEmailAlreadyTakenPersistenceException e){
            throw new RuntimeException();
        }

        Assert.assertNotNull(user);
        Assert.assertEquals(EMAIL, user.getEmail());
        Assert.assertEquals(PASSWORD, user.getPassword());

        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
    }

    @Test(expected = UserEmailAlreadyTakenPersistenceException.class)
    public void testCreateException() throws UserEmailAlreadyTakenPersistenceException {
        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");

        userDao.create(new User.UserBuilder(EMAIL, PASSWORD, USERNAME));

    }

    @Test
    public void testChangePassword(){
        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");

        userDao.changePassword(ID, NEW_PASSWORD);
        String query = "pass = '" + NEW_PASSWORD + "'";
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", query ));
    }

    @Test
    public void testEditProfile(){
        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");

        userDao.editProfile(ID, NEW_USERNAME);
        String query = "username = '" + NEW_USERNAME + "'";
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", query));
    }

}
