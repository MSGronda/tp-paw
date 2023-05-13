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
import java.util.Map;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class SubjectJdbcDaoTest {

    private static final String ID = "72.32";

    private static final String ID2 = "92.75";

    private static final String NAME = "Sistemas";
    private static final String NAME2 = "Economia";

    private static final String DEPARTMENT = "Departamento";
    private static final String DEPARTMENT2 = "matematicas";

    private static final int CREDITS = 3;

    private static final long DEGREEID1 = 1;
    private static final long DEGREEID2 = 2;


    private static final String DEGREENAME1 = "Informatica";

    private static final String DEGREENAME2 = "Industrial";

    private static final int SEMESTER1 = 1;
    private static final int SEMESTER2 = 2;

    private static final int YEAR = 1;



    private JdbcTemplate jdbcTemplate;

    private JdbcTemplate jdbcTemplateDegree;
    private JdbcTemplate jdbcTemplateSubjectDegree;



    @Autowired
    private DataSource ds;

    @Autowired
    private SubjectJdbcDao subjectDao;

    @Before
    public void setup(){
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcTemplateDegree = new JdbcTemplate(ds);
        jdbcTemplateSubjectDegree = new JdbcTemplate(ds);

        JdbcTestUtils.deleteFromTables(jdbcTemplateSubjectDegree, "subjectsdegrees");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "subjects");
        JdbcTestUtils.deleteFromTables(jdbcTemplateDegree, "degrees");
    }

    @Test
    public void testFindById(){
        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + ID + "', '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");

        Optional<Subject> subject = subjectDao.findById(ID);

        Assert.assertTrue(subject.isPresent());
        Assert.assertEquals(DEPARTMENT, subject.get().getDepartment());
    }

    @Test
    public void testGetAllGroupedByDegreeId(){
        jdbcTemplateDegree.execute("INSERT INTO degrees(id, degname) VALUES (" + DEGREEID1 + ", '" + DEGREENAME1 + "' )");
        jdbcTemplateDegree.execute("INSERT INTO degrees(id, degname) VALUES (" + DEGREEID2 + ", '" + DEGREENAME2 + "' )");

        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + ID + "', '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + ID2 + "', '" + NAME2 + "', '" + DEPARTMENT2 + "', " + CREDITS + ")");

        jdbcTemplateSubjectDegree.execute("INSERT INTO subjectsdegrees(idsub, semester, iddeg) VALUES ('" + ID + "', " + SEMESTER1 + ", " + DEGREEID1 + ")");
        jdbcTemplateSubjectDegree.execute("INSERT INTO subjectsdegrees(idsub, semester, iddeg) VALUES ('" + ID2 + "', " + SEMESTER2 + ", " + DEGREEID2 + ")");


        Map<Long, List<Subject>> map = subjectDao.getAllGroupedByDegreeId();

        Assert.assertFalse(map.isEmpty());
        Assert.assertFalse(map.get(DEGREEID1).isEmpty());
        Assert.assertFalse(map.get(DEGREEID2).isEmpty());
        Assert.assertTrue(map.get(DEGREEID1).stream().findFirst().isPresent());
        Assert.assertTrue(map.get(DEGREEID2).stream().findFirst().isPresent());
        Assert.assertEquals(NAME ,map.get(DEGREEID1).stream().findFirst().get().getName());
        Assert.assertEquals(NAME2 ,map.get(DEGREEID2).stream().findFirst().get().getName());

    }

    @Test
    public void testGetAllGroupedByDegIdAndSemester(){
        jdbcTemplateDegree.execute("INSERT INTO degrees(id, degname) VALUES (" + DEGREEID1 + ", '" + DEGREENAME1 + "' )");
        jdbcTemplateDegree.execute("INSERT INTO degrees(id, degname) VALUES (" + DEGREEID2 + ", '" + DEGREENAME2 + "' )");

        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + ID + "', '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + ID2 + "', '" + NAME2 + "', '" + DEPARTMENT2 + "', " + CREDITS + ")");

        jdbcTemplateSubjectDegree.execute("INSERT INTO subjectsdegrees(idsub, semester, iddeg) VALUES ('" + ID + "', " + SEMESTER1 + ", " + DEGREEID1 + ")");
        jdbcTemplateSubjectDegree.execute("INSERT INTO subjectsdegrees(idsub, semester, iddeg) VALUES ('" + ID2 + "', " + SEMESTER2 + ", " + DEGREEID2 + ")");

        Map<Long, Map<Integer, List<Subject>>> supermap = subjectDao.getAllGroupedByDegIdAndSemester();

        Assert.assertFalse(supermap.isEmpty());
        Assert.assertFalse(supermap.get(DEGREEID1).isEmpty());
        Assert.assertFalse(supermap.get(DEGREEID2).isEmpty());
        Assert.assertFalse(supermap.get(DEGREEID1).get(SEMESTER1).isEmpty());
        Assert.assertFalse(supermap.get(DEGREEID2).get(SEMESTER2).isEmpty());

        Assert.assertTrue(supermap.get(DEGREEID1).get(SEMESTER1).stream().findFirst().isPresent());
        Assert.assertTrue(supermap.get(DEGREEID2).get(SEMESTER2).stream().findFirst().isPresent());
        Assert.assertEquals(NAME ,supermap.get(DEGREEID1).get(SEMESTER1).stream().findFirst().get().getName());
        Assert.assertEquals(NAME2 ,supermap.get(DEGREEID2).get(SEMESTER2).stream().findFirst().get().getName());
    }

    @Test
    public void testGetAllGroupedByDegIdAndYear(){
        jdbcTemplateDegree.execute("INSERT INTO degrees(id, degname) VALUES (" + DEGREEID1 + ", '" + DEGREENAME1 + "' )");
        jdbcTemplateDegree.execute("INSERT INTO degrees(id, degname) VALUES (" + DEGREEID2 + ", '" + DEGREENAME2 + "' )");

        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + ID + "', '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + ID2 + "', '" + NAME2 + "', '" + DEPARTMENT2 + "', " + CREDITS + ")");

        jdbcTemplateSubjectDegree.execute("INSERT INTO subjectsdegrees(idsub, semester, iddeg) VALUES ('" + ID + "', " + SEMESTER1 + ", " + DEGREEID1 + ")");
        jdbcTemplateSubjectDegree.execute("INSERT INTO subjectsdegrees(idsub, semester, iddeg) VALUES ('" + ID2 + "', " + SEMESTER2 + ", " + DEGREEID2 + ")");

        Map<Long, Map<Integer, List<Subject>>> supermap = subjectDao.getAllGroupedByDegIdAndYear();

        Assert.assertFalse(supermap.isEmpty());
        Assert.assertFalse(supermap.get(DEGREEID1).isEmpty());
        Assert.assertFalse(supermap.get(DEGREEID2).isEmpty());
        Assert.assertFalse(supermap.get(DEGREEID1).get(YEAR).isEmpty());
        Assert.assertFalse(supermap.get(DEGREEID2).get(YEAR).isEmpty());

        Assert.assertTrue(supermap.get(DEGREEID1).get(YEAR).stream().findFirst().isPresent());
        Assert.assertTrue(supermap.get(DEGREEID2).get(YEAR).stream().findFirst().isPresent());
        Assert.assertEquals(NAME ,supermap.get(DEGREEID1).get(YEAR).stream().findFirst().get().getName());
        Assert.assertEquals(NAME2 ,supermap.get(DEGREEID2).get(YEAR).stream().findFirst().get().getName());

    }

    //A hsqldb no lo gusta el ILIKE que hace el llamado getByName
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
