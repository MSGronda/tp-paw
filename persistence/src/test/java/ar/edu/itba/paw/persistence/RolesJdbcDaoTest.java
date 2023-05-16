package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Roles;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.After;
import ar.edu.itba.paw.persistence.constants.Tables;
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
import javax.xml.crypto.Data;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class RolesJdbcDaoTest {

    private static final long ID = 1;
    private static final long ID2 = 2;

    private static final String NAME = "USER";
    private static final String NAME2 = "EDITOR";


    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource ds;

    @Autowired
    private RolesDao rolesDao;

    @Before
    public void setup(){
        jdbcTemplate = new JdbcTemplate(ds);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "roles");
    }

    @After
    public void clearDb(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, Tables.ROLES);
    }

    @Test
    public void testFindById(){
        jdbcTemplate.execute("INSERT INTO " + Tables.ROLES + " (id, name) VALUES (" + ID + ", '" + NAME + "' )");

        Optional<Roles> role = rolesDao.findById(ID);

        Assert.assertTrue(role.isPresent());
        Assert.assertEquals(NAME, role.get().getName());
    }

    @Test
    public void testGetAll(){
        jdbcTemplate.execute("INSERT INTO " + Tables.ROLES + " (id, name) VALUES (" + ID + ", '" + NAME + "' )");
        jdbcTemplate.execute("INSERT INTO " + Tables.ROLES + " (id, name) VALUES (" + ID2 + ", '" + NAME2 + "' )");

        List<Roles> list = rolesDao.getAll();

        Assert.assertEquals(2, list.size());
        Assert.assertEquals(NAME, list.get(0).getName());
        Assert.assertEquals(NAME2, list.get(1).getName());
    }

    @Test
    public void testFindByName(){
        jdbcTemplate.execute("INSERT INTO " + Tables.ROLES + " (id, name) VALUES (" + ID + ", '" + NAME + "' )");

        Optional<Roles> role = rolesDao.findByName(NAME);

        Assert.assertTrue(role.isPresent());
        Assert.assertEquals(NAME, role.get().getName());
        Assert.assertEquals(ID, role.get().getId().longValue());
    }
}
