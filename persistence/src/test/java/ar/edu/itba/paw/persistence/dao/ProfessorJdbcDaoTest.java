package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.After;
import ar.edu.itba.paw.persistence.constants.Tables;
import ar.edu.itba.paw.persistence.dao.ProfessorJdbcDao;
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
    private static long ID1 = 1L;
    private static long ID2 = 2L;
    private static String SUB_ID1="31.08";
    private static String SUB_ID2="72.03";
    private static String SUB1_NAME="Sistemas";
    private static String SUB2_NAME="Intro";
    private static String SUB1_DEPARTMENT="Ciencias exactas y naturales";
    private static String SUB2_DEPARTMENT="Sistemas Digitales";

    private static Integer SUB1_CREDITS=3;
    private static Integer SUB2_CREDITS=3;


    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource ds;
    @Autowired
    private ProfessorJdbcDao professorDao;

    @Before
    public void setup(){
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @After
    public void clearDb(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, Tables.PROFS_SUBJECTS);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, Tables.PROFS);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, Tables.SUBJECTS);
    }


    @Test
    public void testFindById(){
        jdbcTemplate.execute("INSERT INTO " + Tables.PROFS + " (profname) VALUES ('" + PROF_NAME1 + "')");

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

        jdbcTemplate.execute("INSERT INTO " + Tables.PROFS + " (profname) VALUES ('" + PROF_NAME1 + "')");
        jdbcTemplate.execute("INSERT INTO " + Tables.PROFS + " (profname) VALUES ('" + PROF_NAME2 + "')");

        List<Professor> professors = professorDao.getAll();

        Assert.assertEquals(2,professors.size());
        Assert.assertEquals(PROF_NAME1,professors.get(0).getName());
        Assert.assertEquals(PROF_NAME2,professors.get(1).getName());
    }

    @Test
    public void testGetAllNotFound(){
        List<Professor> professors = professorDao.getAll();

        Assert.assertEquals(0,professors.size());
    }

    @Test
    public void testGetAllBySubject(){
        jdbcTemplate.execute("INSERT INTO professors (id,profname) VALUES ("+ ID1 +",'" + PROF_NAME1 + "')");
        jdbcTemplate.execute("INSERT INTO professors (id,profname) VALUES ("+ ID2 +",'" + PROF_NAME2 + "')");
        jdbcTemplate.execute("INSERT INTO subjects (id,subname,department,credits) VALUES ('"+ SUB_ID1 +"','"+ SUB1_NAME +"','"+ SUB1_DEPARTMENT +"',"+ SUB1_CREDITS +")");
        jdbcTemplate.execute("INSERT INTO subjects (id,subname,department,credits) VALUES ('"+ SUB_ID2 +"','"+ SUB2_NAME +"','"+ SUB2_DEPARTMENT +"',"+ SUB2_CREDITS +")");
        jdbcTemplate.execute("INSERT INTO professorssubjects (idprof,idsub) VALUES (" + ID1 + ",'"+ SUB_ID1 +"')");
        jdbcTemplate.execute("INSERT INTO professorssubjects (idprof,idsub) VALUES (" + ID1 + ",'"+ SUB_ID2 +"')");
        jdbcTemplate.execute("INSERT INTO professorssubjects (idprof,idsub) VALUES (" + ID2 + ",'"+ SUB_ID1 +"')");

        List<Professor> professors = professorDao.getAllBySubject(SUB_ID1);

        Assert.assertEquals(2,professors.size());
        Assert.assertEquals(ID1, professors.get(0).getId());
        Assert.assertEquals(ID2, professors.get(1).getId());
    }

    @Test
    public void testGetAllBySubjectNotFound(){
        List<Professor> professors = professorDao.getAllBySubject(SUB_ID1);

        Assert.assertEquals(0,professors.size());
    }
}
