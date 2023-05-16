package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Professor;
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
public class ProfessorJdbcDaoTest {
    private static String PROF_NAME1 = "Juan Ariel";
    private static String PROF_NAME2 = "Ariel Juan";
    private static Long ID1 = 1L;
    private static Long ID2 = 2L;


    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource ds;
    @Autowired
    private ProfessorJdbcDao professorDao;

    @Before
    public void setup(){
        jdbcTemplate = new JdbcTemplate(ds);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, "professors");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "professorssubjects");
    }

    @Test
    public void testFindById(){
        jdbcTemplate.execute("INSERT INTO professors (profname) VALUES ('" + PROF_NAME1 + "')");

        Optional<Professor> professor = professorDao.findById(ID1);

        Assert.assertTrue(professor.isPresent());
        Assert.assertEquals(PROF_NAME1, professor.get().getName());
    }

    @Test
    public void testFindByIdNotFound(){

        Optional<Professor> professor = professorDao.findById(ID1);

        Assert.assertFalse(professor.isPresent());
    }

    @Test
    public void testGetAll(){

        jdbcTemplate.execute("INSERT INTO professors (profname) VALUES ('" + PROF_NAME1 + "')");
        jdbcTemplate.execute("INSERT INTO professors (profname) VALUES ('" + PROF_NAME2 + "')");

        List<Professor> professors = professorDao.getAll();

        Assert.assertEquals(2,professors.size());
        Assert.assertEquals(PROF_NAME1,professors.get(0).getName());
        Assert.assertEquals(PROF_NAME2,professors.get(1).getName());
    }
}
