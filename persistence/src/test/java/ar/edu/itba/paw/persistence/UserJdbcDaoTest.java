package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Roles;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.exceptions.UserEmailAlreadyTakenPersistenceException;
import org.junit.After;
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
import java.util.Map;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserJdbcDaoTest {
    private static final long ID = 1;

    private static final long ID2 = 2;
    private static final String EMAIL = "email";

    private static final String EMAIL2 = "liame";

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

    private static final String SUBJECTID2 = "23.66";

    private static final String SUBJECTNAME2 = "Algo";

    private static final String DEPARTMENT2 = "otro_departamento";
    private static final int CREDITS2 = 6;
    private static final Integer SUBJECTPROGRESS = 0;
    private static final Integer NEWSUBJECTPROGRESS = 1;

    private static final Boolean CONFIRMED = true;

    private static final Boolean NOTCONFIRMED = false;


    private static final String CONFIRMTOKEN = "asdf";
    private static final String CONFIRMTOKEN2 = "fdsa";


    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource ds;
    @Autowired
    private UserJdbcDao userDao;

    @Before
    public void setup() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @After
    public void clearDb(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "usersubjectprogress");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "userroles");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "roles");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "reviews");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "subjectsdegrees");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "subjects");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "images");
    }

    @Test
    public void testFindById() {
        jdbcTemplate.execute("INSERT INTO images VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username, image_id, CONFIRMTOKEN, CONFIRMED ) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ", '" + CONFIRMTOKEN + "', " + CONFIRMED + ")");

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
    public void testGetAll(){
        jdbcTemplate.execute("INSERT INTO images VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username, image_id, CONFIRMTOKEN, CONFIRMED ) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ", '" + CONFIRMTOKEN + "', " + CONFIRMED + ")");
        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username, image_id, CONFIRMTOKEN, CONFIRMED ) VALUES (" + ID2 + ", '" + EMAIL2 + "', '" + NEW_PASSWORD + "', '" + NEW_USERNAME + "', " + IMAGEID + ", '" + CONFIRMTOKEN2 + "', " + CONFIRMED + ")");

        List<User> list = userDao.getAll();

        Assert.assertFalse(list.isEmpty());
        Assert.assertEquals(EMAIL, list.stream().findFirst().get().getEmail());
        Assert.assertEquals(EMAIL2, list.get(1).getEmail());
    }

    @Test
    public void testGetUserWithEmail(){
        jdbcTemplate.execute("INSERT INTO images VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username, image_id, confirmtoken, confirmed ) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ", '" + CONFIRMTOKEN + "', " + CONFIRMED + ")");


        Optional<User> user = userDao.getUserWithEmail(EMAIL);

        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(ID, user.get().getId());
    }

    @Test
    public void testGetUserWithEmailNotExists(){
        Optional<User> user = userDao.getUserWithEmail(EMAIL);

        Assert.assertFalse(user.isPresent());
    }

    @Test
    public void testCreate() {
        jdbcTemplate.execute("INSERT INTO images VALUES (" + IMAGEID + ", " + IMAGE + ")");

        User user;
        try{
            user = userDao.create(new User.UserBuilder(EMAIL, PASSWORD, USERNAME).imageId(IMAGEID).confirmToken(CONFIRMTOKEN));
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
        jdbcTemplate.execute("INSERT INTO images VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username, image_id, confirmtoken, confirmed ) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ", '" + CONFIRMTOKEN + "', " + CONFIRMED + ")");

        userDao.create(new User.UserBuilder(EMAIL, PASSWORD, USERNAME).confirmToken(CONFIRMTOKEN));

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
        jdbcTemplate.execute("INSERT INTO roles(id, name) VALUES (" + ROLEID + ", '" + ROLENAME + "')");
        jdbcTemplate.execute("INSERT INTO userroles(roleid, userid) VALUES (" + ROLEID + ", " + ID + ")");

        List<Roles> roles = userDao.getUserRoles(ID);

        Assert.assertEquals(1, roles.size());
        Assert.assertEquals(ROLENAME, roles.stream().findFirst().get().getName());
    }

    @Test
    public void testAddIdToUserRoles(){
        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
        jdbcTemplate.execute("INSERT INTO roles(id, name) VALUES (" + ROLEID + ", '" + ROLENAME + "')");

        Integer success = userDao.addIdToUserRoles(ROLEID, ID);

        String query = "roleid = " + ROLEID + " AND userid = " + ID;
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "userroles", query));
    }

    @Test
    public void testGetUserSubjectProgress(){
        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + SUBJECTID + "', '" + SUBJECTNAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO usersubjectprogress(iduser, idsub, subjectstate) VALUES (" + ID + ", '" + SUBJECTID + "', " + SUBJECTPROGRESS + ")" );

        Optional<Integer> subjectP = userDao.getUserSubjectProgress(ID, SUBJECTID);

        Assert.assertTrue(subjectP.isPresent());
        Assert.assertEquals(SUBJECTPROGRESS, subjectP.get());

    }

    @Test
    public void testUserAllSubjectProgress(){
        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + SUBJECTID + "', '" + SUBJECTNAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + SUBJECTID2 + "', '" + SUBJECTNAME2 + "', '" + DEPARTMENT2 + "', " + CREDITS2 + ")");

        jdbcTemplate.execute("INSERT INTO usersubjectprogress(iduser, idsub, subjectstate) VALUES (" + ID + ", '" + SUBJECTID + "', " + SUBJECTPROGRESS + ")" );
        jdbcTemplate.execute("INSERT INTO usersubjectprogress(iduser, idsub, subjectstate) VALUES (" + ID + ", '" + SUBJECTID2 + "', " + NEWSUBJECTPROGRESS + ")" );

        Map<String, Integer> map = userDao.getUserAllSubjectProgress(ID);

        Assert.assertEquals(2, map.size());
        Assert.assertEquals(SUBJECTPROGRESS, map.get(SUBJECTID));
        Assert.assertEquals(NEWSUBJECTPROGRESS, map.get(SUBJECTID2));

    }

    @Test
    public void testUpdateSubjectProgressExisting(){
        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + SUBJECTID + "', '" + SUBJECTNAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO usersubjectprogress(iduser, idsub, subjectstate) VALUES (" + ID + ", '" + SUBJECTID + "', " + SUBJECTPROGRESS + ")" );

        int response = userDao.updateSubjectProgress(ID, SUBJECTID, NEWSUBJECTPROGRESS);

        Assert.assertEquals(1, response);

        String queryShouldNotExist = "iduser = " + ID + " AND idsub = " + SUBJECTID + " AND subjectstate = " + SUBJECTPROGRESS;
        String queryUpdated = "iduser = " + ID + " AND idsub = " + SUBJECTID + " AND subjectstate = " + NEWSUBJECTPROGRESS;
        Assert.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "usersubjectprogress", queryShouldNotExist));
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "usersubjectprogress", queryUpdated));

    }

    @Test
    public void testUpdateSubjectProgressNew(){
        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + SUBJECTID + "', '" + SUBJECTNAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");

        int response = userDao.updateSubjectProgress(ID, SUBJECTID, SUBJECTPROGRESS);

        Assert.assertEquals(1, response);
        String queryShouldExist = "iduser = " + ID + " AND idsub = " + SUBJECTID + " AND subjectstate = " + SUBJECTPROGRESS;

        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "usersubjectprogress", queryShouldExist));
    }


    @Test
    public void testDeleteUserProgressForSubject(){
        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + SUBJECTID + "', '" + SUBJECTNAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO usersubjectprogress(iduser, idsub, subjectstate) VALUES (" + ID + ", '" + SUBJECTID + "', " + SUBJECTPROGRESS + ")" );

        int response = userDao.deleteUserProgressForSubject(ID, SUBJECTID);


        Assert.assertEquals(1, response);
        Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "usersubjectprogress"));
    }

    @Test
    public void testFindUserByConfirmToken(){
        jdbcTemplate.execute("INSERT INTO images VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username, image_id, CONFIRMTOKEN, CONFIRMED ) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ", '" + CONFIRMTOKEN + "', " + CONFIRMED + ")");

        Optional<User> maybeUser = userDao.findUserByConfirmToken(CONFIRMTOKEN);

        Assert.assertTrue(maybeUser.isPresent());
        Assert.assertEquals(USERNAME, maybeUser.get().getUsername());
    }

    @Test
    public void testFindUserByConfirmTokenNotExits(){
        Optional<User> maybeUser = userDao.findUserByConfirmToken(CONFIRMTOKEN);

        Assert.assertFalse(maybeUser.isPresent());
    }

    @Test
    public void testConfirmUser(){
        jdbcTemplate.execute("INSERT INTO images VALUES (" + IMAGEID + ", " + IMAGE + ")");
        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username, image_id, CONFIRMTOKEN, CONFIRMED ) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ", '" + CONFIRMTOKEN + "', " + NOTCONFIRMED + ")");

        userDao.confirmUser(ID);

        String query = "confirmtoken is NULL AND confirmed = true";
        String queryUnconfirmed = "confirmtoken is not null AND confirmed = false";

        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", query));
        Assert.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", queryUnconfirmed));

    }


}
