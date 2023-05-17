package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.SubjectClass;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.dao.SubjectClassDao;
import org.junit.After;
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
import java.sql.Time;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class SubjectClassJdbcDaoTest {

    private static final String CLASSA = "A";
    private static final String CLASSB = "B";


    private static final String SUBJECTID = "72.33";

    private static final String SUBJECTNAME = "Sistemas";

    private static final String DEPARTMENT = "departamento";
    private static final int CREDITS = 3;

    private static final long IDLOCTIME = 1;

    private static final Integer DAY = 3;

    private static final Time STARTTIME = new Time(11);

    private static final Time ENDTIME = new Time(14);
    private static final String CLASS = "101T";
    private static final String BUILDING = "SDT";
    private static final String MODE = "presencial";

    private static final long PROFESSORID = 1;

    private static final String PROFNAME = "Turrin, Marcelo";

    private static final long USERID = 1;

    private static final String EMAIL = "email";

    private static final String PASSWORD = "pass";

    private static final String USERNAME = "username";
    private static final Integer SUBJECTPROGRESS = 0;
    private static final Integer SUBJECTPROGRESSCOMPLETE = 1;


    private static final String PREREQID = "31.25";

    private static final String PREREQNAME = "Antes de Sistemas";

    private static final String PREREQDEPARTMENT = "departamento-1";
    private static final int PREREQCREDITS = 6;


    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource ds;

    @Autowired
    private SubjectClassDao subjectClassDao;

    @Before
    public void setup(){
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @After
    public void clearDb(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "prereqsubjects");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "usersubjectprogress");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "classloctime");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "classprofessors");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "class");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "subjects");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "professors");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
    }

    @Test
    public void testGetBySubIdRaw(){
        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + SUBJECTID + "', '" + SUBJECTNAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO class(idsub, idclass) VALUES ('" + SUBJECTID + "', '" + CLASSA + "')");
        jdbcTemplate.execute("INSERT INTO class(idsub, idclass) VALUES ('" + SUBJECTID + "', '" + CLASSB + "')");

        List<SubjectClass> list = subjectClassDao.getBySubIdRaw(SUBJECTID);

        Assert.assertEquals(2, list.size());
        Assert.assertEquals(CLASSA, list.get(0).getIdClass());
        Assert.assertEquals(CLASSB, list.get(1).getIdClass());

    }

    @Test
    public void testGetBySubId(){
        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + SUBJECTID + "', '" + SUBJECTNAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO class(idsub, idclass) VALUES ('" + SUBJECTID + "', '" + CLASSA + "')");
        jdbcTemplate.execute("INSERT INTO classloctime(idloctime, idsub, idclass, day, starttime, endtime, class, building, mode) " +
                "VALUES (" + IDLOCTIME + ", '" + SUBJECTID + "', '" + CLASSA + "', " + DAY + ", '" + STARTTIME + "', '" + ENDTIME + "', '" + CLASS + "', '" + BUILDING + "', '" + MODE + "')");
        jdbcTemplate.execute("INSERT INTO professors(id, profname) VALUES (" + PROFESSORID + ", '" + PROFNAME + "')" );
        jdbcTemplate.execute("INSERT INTO classprofessors(idsub, idclass, idprof) VALUES ('" + SUBJECTID + "', '" + CLASSA + "', " + PROFESSORID + ")");

        List<SubjectClass> list = subjectClassDao.getBySubId(SUBJECTID);

        Assert.assertEquals(1, list.size());
        Assert.assertEquals(BUILDING, list.get(0).getClassTimes().stream().findFirst().get().getBuilding());
        Assert.assertEquals(PROFESSORID, list.get(0).getProfessors().stream().findFirst().get().getId());
    }

    @Test
    public void testGetAll(){
        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + SUBJECTID + "', '" + SUBJECTNAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO class(idsub, idclass) VALUES ('" + SUBJECTID + "', '" + CLASSA + "')");
        jdbcTemplate.execute("INSERT INTO classloctime(idloctime, idsub, idclass, day, starttime, endtime, class, building, mode) " +
                "VALUES (" + IDLOCTIME + ", '" + SUBJECTID + "', '" + CLASSA + "', " + DAY + ", '" + STARTTIME + "', '" + ENDTIME + "', '" + CLASS + "', '" + BUILDING + "', '" + MODE + "')");
        jdbcTemplate.execute("INSERT INTO professors(id, profname) VALUES (" + PROFESSORID + ", '" + PROFNAME + "')" );
        jdbcTemplate.execute("INSERT INTO classprofessors(idsub, idclass, idprof) VALUES ('" + SUBJECTID + "', '" + CLASSA + "', " + PROFESSORID + ")");

        List<SubjectClass> list = subjectClassDao.getAll();

        Assert.assertEquals(1, list.size());
        Assert.assertEquals(BUILDING, list.get(0).getClassTimes().stream().findFirst().get().getBuilding());
        Assert.assertEquals(PROFESSORID, list.get(0).getProfessors().stream().findFirst().get().getId());
    }

    @Test
    public void testGetAllSubsWithClassThatUserCanDo(){
        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + SUBJECTID + "', '" + SUBJECTNAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES ('" + PREREQID + "', '" + PREREQNAME + "', '" + PREREQDEPARTMENT + "', " + PREREQCREDITS + ")");
        jdbcTemplate.execute("INSERT INTO class(idsub, idclass) VALUES ('" + SUBJECTID + "', '" + CLASSA + "')");
        jdbcTemplate.execute("INSERT INTO classloctime(idloctime, idsub, idclass, day, starttime, endtime, class, building, mode) " +
                "VALUES (" + IDLOCTIME + ", '" + SUBJECTID + "', '" + CLASSA + "', " + DAY + ", '" + STARTTIME + "', '" + ENDTIME + "', '" + CLASS + "', '" + BUILDING + "', '" + MODE + "')");
        jdbcTemplate.execute("INSERT INTO professors(id, profname) VALUES (" + PROFESSORID + ", '" + PROFNAME + "')" );
        jdbcTemplate.execute("INSERT INTO classprofessors(idsub, idclass, idprof) VALUES ('" + SUBJECTID + "', '" + CLASSA + "', " + PROFESSORID + ")");
//        jdbcTemplate.execute("INSERT INTO usersubjectprogress(iduser, idsub, subjectstate) VALUES (" + USERID + ", '" + SUBJECTID + "', " + SUBJECTPROGRESS + ")" );
        jdbcTemplate.execute("INSERT INTO usersubjectprogress(iduser, idsub, subjectstate) VALUES (" + USERID + ", '" + PREREQID + "', " + SUBJECTPROGRESSCOMPLETE + ")" );
        jdbcTemplate.execute("INSERT INTO prereqsubjects(idsub, idprereq) VALUES ('" + SUBJECTID + "', '" + PREREQID + "')");

        List<Subject> list = subjectClassDao.getAllSubsWithClassThatUserCanDo(USERID);

        Assert.assertEquals(1, list.size());
        Assert.assertEquals(SUBJECTNAME, list.get(0).getName());
        Assert.assertEquals(CLASSA, list.get(0).getSubjectClasses().get(SUBJECTID+CLASSA).getIdClass());
        Assert.assertEquals(BUILDING, list.get(0).getSubjectClasses().get(SUBJECTID+CLASSA).getClassTimes().stream().findFirst().get().getBuilding());
        Assert.assertEquals(CLASS, list.get(0).getSubjectClasses().get(SUBJECTID+CLASSA).getClassTimes().stream().findFirst().get().getClassLoc());
        Assert.assertEquals(DAY, list.get(0).getSubjectClasses().get(SUBJECTID+CLASSA).getClassTimes().stream().findFirst().get().getDay());

    }
}
