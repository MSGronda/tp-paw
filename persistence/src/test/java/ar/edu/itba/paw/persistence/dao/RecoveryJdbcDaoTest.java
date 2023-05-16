package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.After;
import ar.edu.itba.paw.persistence.constants.Tables;
import ar.edu.itba.paw.persistence.dao.RecoveryDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class RecoveryJdbcDaoTest {

    private static final long ID = 1;
    private static final String EMAIL = "email";
    private static final String PASSWORD = "pass";
    private static final String USERNAME = "username";
    private static final String TOKEN = "asdffdsa";


    private static final Date date = new Date(System.currentTimeMillis());
    private static final Timestamp CREATED = new Timestamp(date.getTime());

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource ds;

    @Autowired
    private RecoveryDao recoveryDao;

    @Before
    public void setup(){
        jdbcTemplate = new JdbcTemplate(ds);
    }

    //TODO - Revisar, creo que falla por sintaxis de hqsldb
//    @Test
//    public void testCreate(){
//        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
//
//        recoveryDao.create(TOKEN, ID);
//
//        String query = "token = " + TOKEN + " AND userid = " + ID;
//        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "recoverytoken", query));
//    }


    @After
    public void clearDb(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, Tables.RECOVERY);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, Tables.USERS);
    }

    @Test
    public void testfindUserIdByToken(){
        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
        jdbcTemplate.execute("INSERT INTO " + Tables.RECOVERY + " (token, userid, created) VALUES ('" + TOKEN + "', " + ID + ", '" + CREATED + "')");

        Optional<Long> userId = recoveryDao.findUserIdByToken(TOKEN);

        Assert.assertTrue(userId.isPresent());
        Assert.assertEquals(ID, userId.get().longValue());
    }

    @Test
    public void testDeleteWithToken(){
        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
        jdbcTemplate.execute("INSERT INTO " + Tables.RECOVERY + " (token, userid, created) VALUES ('" + TOKEN + "', " + ID + ", '" + CREATED + "')");

        recoveryDao.delete(TOKEN);

        Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, Tables.RECOVERY));
    }

    @Test
    public void testDeleteWithUserId(){
        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username) VALUES (" + ID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
        jdbcTemplate.execute("INSERT INTO " + Tables.RECOVERY + " (token, userid, created) VALUES ('" + TOKEN + "', " + ID + ", '" + CREATED + "')");

        recoveryDao.delete(ID);

        Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, Tables.RECOVERY));
    }

}
