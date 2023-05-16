package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Semester;
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
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class DegreeJdbcDaoTest {
    private static final long ID = 1;
    private static final long ID2 = 2;
    private static final String NAME = "Ingieneria en Informatica";
    private static final String NAME2 = "Ingieneria Mecanica";
    private static final String SUBJECTID = "72.32";
    private static final String SUBJECTID2 = "92.75";
    private static final String SUBJECTNAME = "Sistemas";
    private static final String SUBJECTNAME2 = "Economia";
    private static final String DEPARTMENT = "Departamento";
    private static final String DEPARTMENT2 = "matematicas";
    private static final int CREDITS = 3;
    private static final int SEMESTER1 = 1;
    private static final int SEMESTER2 = 2;


    @Autowired
    private DataSource ds;

    @Autowired
    private DegreeJdbcDao degreeDao;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void testSetup(){
        jdbcTemplate = new JdbcTemplate(ds);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, "degrees");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "subjects");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "subjectsdegrees");

    }

    @Test
    public void testFindById() {
        jdbcTemplate.execute("INSERT INTO degrees(id, degname) VALUES (" + ID + ", '" + NAME + "')");
        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + SUBJECTID + "', '" + SUBJECTNAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + SUBJECTID2 + "', '" + SUBJECTNAME2 + "', '" + DEPARTMENT2 + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO subjectsdegrees(idsub, semester, iddeg) VALUES ('" + SUBJECTID + "', " + SEMESTER1 + ", " + ID + ")");
        jdbcTemplate.execute("INSERT INTO subjectsdegrees(idsub, semester, iddeg) VALUES ('" + SUBJECTID2 + "', " + SEMESTER2 + ", " + ID + ")");

        Optional<Degree> degree = degreeDao.findById(ID);

        Assert.assertTrue(degree.isPresent());
        Assert.assertEquals(ID, degree.get().getId());
        Assert.assertEquals(NAME, degree.get().getName());
        Assert.assertEquals(2, degree.get().getSemesters().size());
        Assert.assertEquals(SEMESTER1, degree.get().getSemesters().get(0).getNumber());
        Assert.assertEquals(SEMESTER2, degree.get().getSemesters().get(1).getNumber());
        Assert.assertEquals(1, degree.get().getSemesters().get(0).getSubjectIds().size());
        Assert.assertEquals(1, degree.get().getSemesters().get(1).getSubjectIds().size());
        Assert.assertEquals(SUBJECTID, degree.get().getSemesters().get(0).getSubjectIds().get(0));
        Assert.assertEquals(SUBJECTID2, degree.get().getSemesters().get(1).getSubjectIds().get(0));

    }
    @Test
    public void testGetAll() {
        jdbcTemplate.execute("INSERT INTO degrees(id, degname) VALUES (" + ID + ", '" + NAME + "')");
        jdbcTemplate.execute("INSERT INTO degrees(id, degname) VALUES (" + ID2 + ", '" + NAME2 + "')" );

        List<Degree> degrees = degreeDao.getAll();

        Assert.assertEquals(2, degrees.size());
        Assert.assertEquals(ID, degrees.get(0).getId());
        Assert.assertEquals(NAME, degrees.get(0).getName());
        Assert.assertEquals(ID2, degrees.get(1).getId());
        Assert.assertEquals(NAME2, degrees.get(1).getName());
    }

    @Test
    public void testCreate() {

        Degree degree = degreeDao.create(NAME);
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "degrees"));
        Assert.assertEquals(ID, degree.getId());
        Assert.assertEquals(NAME, degree.getName());
        Degree degree2 = degreeDao.create(NAME2);
        Assert.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "degrees"));
        Assert.assertEquals(ID2, degree2.getId());
        Assert.assertEquals(NAME2, degree2.getName());
    }
}
