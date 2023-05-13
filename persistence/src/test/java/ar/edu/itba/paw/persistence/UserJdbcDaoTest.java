package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Roles;
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
import java.util.List;
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

    private static final long ROLEID = 2;

    private static final String ROLENAME = "EDITOR";

    private static final String SUBJECTID = "72.33";

    private static final String SUBJECTNAME = "Sistemas";

    private static final String DEPARTMENT = "departamento";
    private static final int CREDITS = 3;
    private static final Integer SUBJECTPROGRESS = 0;
    private static final int NEWSUBJECTPROGRESS = 1;



    private JdbcTemplate jdbcTemplate;

    private JdbcTemplate jdbcTemplateSubjectP;

    private JdbcTemplate jdbcTemplateImage;

    private JdbcTemplate jdbcTemplateUserRoles;

    private JdbcTemplate jdbcTemplateRoles;

    private JdbcTemplate jdbcTemplateSubjects;

    @Autowired
    private DataSource ds;
    @Autowired
    private UserJdbcDao userDao;

    @Before
    public void setup() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcTemplateSubjectP = new JdbcTemplate(ds);
        jdbcTemplateImage = new JdbcTemplate(ds);
        jdbcTemplateRoles = new JdbcTemplate(ds);
        jdbcTemplateUserRoles = new JdbcTemplate(ds);
        jdbcTemplateSubjects = new JdbcTemplate(ds);

        JdbcTestUtils.deleteFromTables(jdbcTemplateSubjectP, "usersubjectprogress");
        JdbcTestUtils.deleteFromTables(jdbcTemplateUserRoles, "userroles");
        JdbcTestUtils.deleteFromTables(jdbcTemplateRoles, "roles");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
        JdbcTestUtils.deleteFromTables(jdbcTemplateSubjects, "subjects");
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

    @Test
    public void testGetUserRoles(){
        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
        jdbcTemplateRoles.execute("INSERT INTO roles(id, name) VALUES (" + ROLEID + ", '" + ROLENAME + "')");
        jdbcTemplateUserRoles.execute("INSERT INTO userroles(roleid, userid) VALUES (" + ROLEID + ", " + ID + ")");

        List<Roles> roles = userDao.getUserRoles(ID);

        Assert.assertEquals(1, roles.size());
        Assert.assertEquals(ROLENAME, roles.stream().findFirst().get().getName());
    }

    @Test
    public void testAddIdToUserRoles(){
        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
        jdbcTemplateRoles.execute("INSERT INTO roles(id, name) VALUES (" + ROLEID + ", '" + ROLENAME + "')");

        Integer success = userDao.addIdToUserRoles(ROLEID, ID);

        String query = "roleid = " + ROLEID + " AND userid = " + ID;
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplateUserRoles, "userroles", query));
    }

    @Test
    public void testGetUserSubjectProgress(){
        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
        jdbcTemplateSubjects.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + SUBJECTID + "', '" + SUBJECTNAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplateSubjectP.execute("INSERT INTO usersubjectprogress VALUES (" + ID + ", '" + SUBJECTID + "', " + SUBJECTPROGRESS + ")" );

        Optional<Integer> subjectP = userDao.getUserSubjectProgress(ID, SUBJECTID);

        Assert.assertTrue(subjectP.isPresent());
        Assert.assertEquals(SUBJECTPROGRESS, subjectP.get());

    }

    @Test
    public void testUpdateSubjectProgressExisting(){
        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
        jdbcTemplateSubjects.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + SUBJECTID + "', '" + SUBJECTNAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplateSubjectP.execute("INSERT INTO usersubjectprogress VALUES (" + ID + ", '" + SUBJECTID + "', " + SUBJECTPROGRESS + ")" );

        int response = userDao.updateSubjectProgress(ID, SUBJECTID, NEWSUBJECTPROGRESS);

        Assert.assertEquals(1, response);

        String queryShouldNotExist = "iduser = " + ID + " AND idsub = " + SUBJECTID + " AND subjectstate = " + SUBJECTPROGRESS;
        String queryUpdated = "iduser = " + ID + " AND idsub = " + SUBJECTID + " AND subjectstate = " + NEWSUBJECTPROGRESS;
        Assert.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplateSubjectP, "usersubjectprogress", queryShouldNotExist));
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplateSubjectP, "usersubjectprogress", queryUpdated));

    }

    @Test
    public void testUpdateSubjectProgressNew(){
        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
        jdbcTemplateSubjects.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + SUBJECTID + "', '" + SUBJECTNAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");

        int response = userDao.updateSubjectProgress(ID, SUBJECTID, SUBJECTPROGRESS);

        Assert.assertEquals(1, response);
        String queryShouldExist = "iduser = " + ID + " AND idsub = " + SUBJECTID + " AND subjectstate = " + SUBJECTPROGRESS;

        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplateSubjectP, "usersubjectprogress", queryShouldExist));
    }


    @Test
    public void testDeleteUserProgressForSubject(){
        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
        jdbcTemplateSubjects.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + SUBJECTID + "', '" + SUBJECTNAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplateSubjectP.execute("INSERT INTO usersubjectprogress VALUES (" + ID + ", '" + SUBJECTID + "', " + SUBJECTPROGRESS + ")" );

        int response = userDao.deleteUserProgressForSubject(ID, SUBJECTID);


        Assert.assertEquals(1, response);
        Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplateSubjectP, "usersubjectprogress"));
    }

}
