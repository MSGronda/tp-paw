package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.OrderDir;
import ar.edu.itba.paw.models.enums.SubjectOrderField;
import ar.edu.itba.paw.models.exceptions.SubjectClassIdAlreadyExistsException;
import ar.edu.itba.paw.models.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.mock.DegreeMockData;
import ar.edu.itba.paw.persistence.mock.PageMockData;
import ar.edu.itba.paw.persistence.mock.SubjectMockData;
import ar.edu.itba.paw.persistence.mock.UserMockData;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class SubjectJpaDaoTest {

    @Autowired
    private DataSource dataSource;
    @PersistenceContext
    private EntityManager em;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SubjectJpaDao subjectJpaDao;


    @Before
    public void setup() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Rollback
    @Test
    public void testFindById() {
        final Optional<Subject> actual = subjectJpaDao.findById(SubjectMockData.SUB1_ID);

        assertTrue(actual.isPresent());
        assertEquals(SubjectMockData.SUB1_ID, actual.get().getId());
        assertEquals(SubjectMockData.SUB1_NAME, actual.get().getName());
        assertEquals(SubjectMockData.SUB1_DEPARTMENT, actual.get().getDepartment());
        assertEquals(SubjectMockData.SUB1_CREDITS, actual.get().getCredits().intValue());
    }

    @Rollback
    @Test
    public void testCreate() {
        final Subject newSubject = Subject.builder().id("11.17").name("Test Subject 3").department("Quimica").credits(3).build();

        final Subject persistedSubject = subjectJpaDao.create(newSubject);
        em.flush();

        assertNotNull(persistedSubject);
        assertEquals(persistedSubject.getId(), newSubject.getId());
        assertEquals(persistedSubject.getName(), newSubject.getName());
        assertEquals(persistedSubject.getDepartment(), newSubject.getDepartment());
        assertEquals(persistedSubject.getCredits(), newSubject.getCredits());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "subjects", "id = '" + newSubject.getId() + "'"));
    }

    @Rollback
    @Test
    public void testSearch() {
//        final User user = User.builder().id(1).degree(DegreeMockData.getDegree1()).build();
//
//        final List<Subject> subjects = subjectJpaDao.search(user, "Test", PageMockData.DEFAULT_PAGE, new HashMap<>(), PageMockData.DEFAULT_ORDER_SUBJECT, PageMockData.DEFAULT_DIR);
//
//        assertEquals(3, subjects.size());
//        assertTrue(subjects.contains(SubjectMockData.getSubject1()));
//        assertTrue(subjects.contains(SubjectMockData.getSubject2()));
//        assertTrue(subjects.contains(SubjectMockData.getSubject3()));
    }

    @Rollback
    @Test
    public void testAddPrerequisites() {
        final Subject subject = em.find(Subject.class, SubjectMockData.SUB1_ID);

        subjectJpaDao.addPrerequisites(subject, new ArrayList<>(Collections.singletonList(SubjectMockData.SUB2_ID)));
        em.flush();

        assertTrue(subject.getPrerequisites().contains(SubjectMockData.getSubject2()));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate,
                "prereqsubjects",
                "idsub = '" + subject.getId() + "' AND idprereq = '" + SubjectMockData.SUB2_ID + "'"
        ));
    }

    @Rollback
    @Test(expected = SubjectNotFoundException.class)
    public void testAddInvalidPrerequisite() {
        final Subject subject = SubjectMockData.getSubject1();
        subjectJpaDao.addPrerequisites(subject, new ArrayList<>(Collections.singleton("Invalid")));

        Assert.fail("SubjectNotFoundException must be thrown");
    }

    @Rollback
    @Test
    public void testAddClass() {
        final String classCode = "A";
        final Subject subject = em.find(Subject.class, SubjectMockData.SUB1_ID);

        subjectJpaDao.addClassToSubject(subject, classCode);
        em.flush();

        assertTrue(subject.getClassById(classCode).isPresent());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate,
                "class",
                "idsub = '" + subject.getId() + "' AND idclass = '" + classCode + "'"
        ));
    }

    @Rollback
    @Test(expected = SubjectClassIdAlreadyExistsException.class)
    public void testAddClassThatAlreadyExists() {
        final Subject subject = em.find(Subject.class, SubjectMockData.SUB1_ID);

        subjectJpaDao.addClassToSubject(subject, "A");
        subjectJpaDao.addClassToSubject(subject, "A");

        Assert.fail("SubjectClassIdAlreadyExistsException should be thrown.");
    }

    @Rollback
    @Test
    public void testEditSubject() {
        final String newName = "New Name";
        final String newDepartment = "Department";
        final Integer newCredits = 6;
        final Set<Subject> newPrerequisites = new HashSet<>(Collections.singletonList(SubjectMockData.getSubject2()));

        final Subject newSub = subjectJpaDao.editSubject(SubjectMockData.getSubject3(), newName, newDepartment, newCredits, newPrerequisites);

        assertEquals(newName, newSub.getName());
        assertEquals(newDepartment, newSub.getDepartment());
        assertEquals(newCredits, newSub.getCredits());
        assertEquals(newPrerequisites, newSub.getPrerequisites());
    }


    @Rollback
    @Test
    public void testAddClassTimes() {
        final Subject subject = em.find(Subject.class, SubjectMockData.SUB3_ID);
        final SubjectClass subjectClass = subjectJpaDao.addClassToSubject(subject, "A");
        final List<Integer> days = new ArrayList<>(Collections.singletonList(1));
        final List<LocalTime> startTimes = new ArrayList<>(Collections.singletonList(LocalTime.MIDNIGHT));
        final List<LocalTime> endTimes = new ArrayList<>(Collections.singletonList(LocalTime.NOON));
        final List<String> locations = new ArrayList<>(Collections.singletonList("201F"));
        final List<String> buildings = new ArrayList<>(Collections.singletonList("Financiero"));
        final List<String> modes = new ArrayList<>(Collections.singletonList("In Person"));

        final List<SubjectClassTime> classTimes = subjectJpaDao.addClassTimesToClass(subjectClass, days, startTimes, endTimes, locations, buildings, modes);
        em.flush();

        assertEquals(1, classTimes.size());
        final SubjectClassTime newSubjectClassTime = classTimes.get(0);
        assertEquals(days.get(0).intValue(), newSubjectClassTime.getDay());
        assertEquals(startTimes.get(0), newSubjectClassTime.getStartTime());
        assertEquals(endTimes.get(0), newSubjectClassTime.getEndTime());
        assertEquals(locations.get(0), newSubjectClassTime.getClassLoc());
        assertEquals(buildings.get(0), newSubjectClassTime.getBuilding());
        assertEquals(modes.get(0), newSubjectClassTime.getMode());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate,
                "classloctime",
                "idloctime = " + newSubjectClassTime.getId()
        ));
    }
}
