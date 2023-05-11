package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.persistence.config.TestConfig;
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
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class SubjectJdbcDaoTest {

    private static final String ID = "72.32";

    private static final String NAME = "Sistemas";
    private static final String DEPARTMENT = "DEpartamento";
    private static final int CREDITS = 3;



    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource ds;

    @Autowired
    private SubjectJdbcDao subjectDao;

    @Before
    public void setup(){
        jdbcTemplate = new JdbcTemplate(ds);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, "subjects");
    }

    @Test
    public void testFindById(){
        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + ID + "', '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");

        Optional<Subject> subject = subjectDao.findById(ID);

        Assert.assertTrue(subject.isPresent());
        Assert.assertEquals(DEPARTMENT, subject.get().getDepartment());
    }

//    @Test
//    public void testFindByName(){
//        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + ID + "', '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
//
//        List<Subject> subjects = subjectDao.getByName(NAME);
//
//        Assert.assertFalse(subjects.isEmpty());
//        Assert.assertEquals(DEPARTMENT, subjects.stream().findFirst().get().getDepartment());
//
//    }
}
