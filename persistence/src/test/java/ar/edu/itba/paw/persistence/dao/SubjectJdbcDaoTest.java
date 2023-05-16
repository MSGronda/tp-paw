package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.After;
import ar.edu.itba.paw.persistence.constants.Tables;
import ar.edu.itba.paw.persistence.dao.SubjectJdbcDao;
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

    private static final long USERID = 1;
    private static final String EMAIL = "email";

    private static final String PASSWORD = "pass";
    private static final String USERNAME = "username";

    private static final Integer SUBJECTPROGRESS = 1;

    private static final Integer NOSUBJECTPROGRESS = 0;

    private static final long REVIEWID = 1;
    private static final long REVIEWID2 = 2;


    private static final int EASY = 1;

    private static final int TIMEDEMANDING = 0;

    private static final String TEXT = "esto es un texto";

    private static final Boolean ANONYMOUS = false;


    private JdbcTemplate jdbcTemplate;



    @Autowired
    private DataSource ds;

    @Autowired
    private SubjectJdbcDao subjectDao;

    @Before
    public void setup(){
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @After
    public void clearDb(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, Tables.USER_SUBJECT_PROGRESS);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, Tables.REVIEWS);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, Tables.USERS);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, Tables.SUBJECTS_DEGREES);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, Tables.SUBJECTS);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, Tables.DEGREES);
    }

    @Test
    public void testFindById(){
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES ('" + ID + "', '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");

        Optional<Subject> subject = subjectDao.findById(ID);

        Assert.assertTrue(subject.isPresent());
        Assert.assertEquals(DEPARTMENT, subject.get().getDepartment());
    }

    @Test
    public void testFindByIdNotPresent(){

        Optional<Subject> subject = subjectDao.findById(ID);

        Assert.assertFalse(subject.isPresent());
    }

    @Test
    public void testFindByIds(){
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES ('" + ID + "', '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES ('" + ID2 + "', '" + NAME2+ "', '" + DEPARTMENT2 + "', " + CREDITS + ")");
        List<String> list = new ArrayList<>();
        list.add(ID);
        list.add(ID2);

        List<Subject> subjectList = subjectDao.findByIds(list);

        Assert.assertEquals(2, subjectList.size());
        Assert.assertEquals(NAME, subjectList.get(0).getName());
        Assert.assertEquals(NAME2, subjectList.get(1).getName());

    }

    @Test
    public void testGetAll(){
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES ('" + ID + "', '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES ('" + ID2 + "', '" + NAME2+ "', '" + DEPARTMENT2 + "', " + CREDITS + ")");

        List<Subject> subjectList = subjectDao.getAll();
        Assert.assertEquals(2, subjectList.size());
        Assert.assertEquals(NAME, subjectList.get(0).getName());
        Assert.assertEquals(NAME2, subjectList.get(1).getName());

    }

    @Test
    public void testGetAllByDegree(){
        jdbcTemplate.execute("INSERT INTO " + Tables.DEGREES + " (id, degname) VALUES (" + DEGREEID1 + ", '" + DEGREENAME1 + "')");
        jdbcTemplate.execute("INSERT INTO " + Tables.DEGREES + " (id, degname) VALUES (" + DEGREEID2 + ", '" + DEGREENAME2 + "')");

        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES ('" + ID + "', '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES ('" + ID2 + "', '" + NAME2+ "', '" + DEPARTMENT2 + "', " + CREDITS + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS_DEGREES + " (idsub, semester, iddeg) VALUES ('" + ID + "', " + SEMESTER1 + ", " + DEGREEID1 + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS_DEGREES + " (idsub, semester, iddeg) VALUES ('" + ID2 + "', " + SEMESTER1 + ", " + DEGREEID1 + ")");
        jdbcTemplate.execute("INSERT INTO subjectsdegrees(idsub, semester, iddeg) VALUES ('" + ID2 + "', " + SEMESTER1 + ", " + DEGREEID2 + ")");

        List<Subject> list = subjectDao.getAllByDegree(DEGREEID1);

        Assert.assertEquals(2, list.size());
        Assert.assertEquals(NAME, list.get(0).getName());
        Assert.assertEquals(NAME2, list.get(1).getName());


    }

    @Test
    public void testGetAllGroupedByDegreeId(){
        jdbcTemplate.execute("INSERT INTO " + Tables.DEGREES + " (id, degname) VALUES (" + DEGREEID1 + ", '" + DEGREENAME1 + "' )");
        jdbcTemplate.execute("INSERT INTO " + Tables.DEGREES + " (id, degname) VALUES (" + DEGREEID2 + ", '" + DEGREENAME2 + "' )");

        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES ('" + ID + "', '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES ('" + ID2 + "', '" + NAME2 + "', '" + DEPARTMENT2 + "', " + CREDITS + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS_DEGREES + " (idsub, semester, iddeg) VALUES ('" + ID + "', " + SEMESTER1 + ", " + DEGREEID1 + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS_DEGREES + " (idsub, semester, iddeg) VALUES ('" + ID2 + "', " + SEMESTER2 + ", " + DEGREEID2 + ")");


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
        jdbcTemplate.execute("INSERT INTO " + Tables.DEGREES + " (id, degname) VALUES (" + DEGREEID1 + ", '" + DEGREENAME1 + "' )");
        jdbcTemplate.execute("INSERT INTO " + Tables.DEGREES + " (id, degname) VALUES (" + DEGREEID2 + ", '" + DEGREENAME2 + "' )");

        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES ('" + ID + "', '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES ('" + ID2 + "', '" + NAME2 + "', '" + DEPARTMENT2 + "', " + CREDITS + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS_DEGREES + " (idsub, semester, iddeg) VALUES ('" + ID + "', " + SEMESTER1 + ", " + DEGREEID1 + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS_DEGREES + " (idsub, semester, iddeg) VALUES ('" + ID2 + "', " + SEMESTER2 + ", " + DEGREEID2 + ")");

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
        jdbcTemplate.execute("INSERT INTO " + Tables.DEGREES + " (id, degname) VALUES (" + DEGREEID1 + ", '" + DEGREENAME1 + "' )");
        jdbcTemplate.execute("INSERT INTO " + Tables.DEGREES + " (id, degname) VALUES (" + DEGREEID2 + ", '" + DEGREENAME2 + "' )");

        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES ('" + ID + "', '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES ('" + ID2 + "', '" + NAME2 + "', '" + DEPARTMENT2 + "', " + CREDITS + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS_DEGREES + " (idsub, semester, iddeg) VALUES ('" + ID + "', " + SEMESTER1 + ", " + DEGREEID1 + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS_DEGREES + " (idsub, semester, iddeg) VALUES ('" + ID2 + "', " + SEMESTER2 + ", " + DEGREEID2 + ")");

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

    //hsqlDB no acepta este interval time, no se puede testear sin cambiar la sintaxis de sql (que funciona en la aplicacion)
//    @Test
//    public void testGetAllUserUnreviewedNotIfSubjects(){
//        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
//
//        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + ID + "', '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
//        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + ID2 + "', '" + NAME2 + "', '" + DEPARTMENT2 + "', " + CREDITS + ")");
//
//        jdbcTemplate.execute("INSERT INTO reviews(id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) VALUES (" + REVIEWID + ", "  + USERID + ", '" + ID + "', null, " + EASY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")");
//        jdbcTemplate.execute("INSERT INTO reviews(id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) VALUES (" + REVIEWID2 + ", "  + USERID + ", '" + ID2 + "', null, " + EASY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")");
//
//
//        jdbcTemplate.execute("INSERT INTO usersubjectprogress(iduser, idsub, subjectstate) VALUES (" + USERID + ", '" + ID + "', " + SUBJECTPROGRESS + ")" );
//        jdbcTemplate.execute("INSERT INTO usersubjectprogress(iduser, idsub, subjectstate) VALUES (" + USERID + ", '" + ID2 + "', " + NOSUBJECTPROGRESS + ")" );
//
//        Map<User, Set<Subject>> map = subjectDao.getAllUserUnreviewedNotifSubjects();
//
//        User user = new User(new User.UserBuilder(EMAIL, PASSWORD, USERNAME).id(USERID));
//
//        Assert.assertEquals(1, map.size());
//        Assert.assertEquals(2, map.get(user).size());
//
//
//    }
}
