package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.OrderDir;
import ar.edu.itba.paw.models.enums.SubjectOrderField;
import ar.edu.itba.paw.models.exceptions.SubjectClassIdAlreadyExistsException;
import ar.edu.itba.paw.models.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.persistence.config.TestConfig;
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
    private static final User user = User.builder().id(1).degree(Degree.builder().id(1).build()).build();
    private static final Subject subject = Subject.builder().id("11.15").name("Test Subject").department("Informatica").credits(6).build();
    private static final Subject subject2 = Subject.builder().id("11.16").name("Test Subject 2").department("Informatica").credits(3).build();

    private static final Subject subject4 = Subject.builder().id("11.18").name("Test Subject 4").department("Informatica").credits(5).build();

    private static final int DEFAULT_PAGE = 1;
    private static final SubjectOrderField DEFAULT_ORDER = SubjectOrderField.ID;
    private  static final OrderDir DEFAULT_DIR = OrderDir.ASCENDING;

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
        final Optional<Subject> actual = subjectJpaDao.findById(subject.getId());

        assertTrue(actual.isPresent());
        assertEquals(subject.getId(), actual.get().getId());
        assertEquals(subject.getName(), actual.get().getName());
        assertEquals(subject.getDepartment(), actual.get().getDepartment());
        assertEquals(subject.getCredits(), actual.get().getCredits());
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
    }

    @Rollback
    @Test
    public void testSearch() {
//        final List<Subject> subjects = subjectJpaDao.search(user, "Test", DEFAULT_PAGE, new HashMap<>(), DEFAULT_ORDER, DEFAULT_DIR);
//
//        assertEquals(2, subjects.size());
//        assertTrue(subjects.contains(subject));
//        assertTrue(subjects.contains(subject2));
    }

    @Rollback
    @Test
    public void testFindAllUserHasDone() {
        final List<Subject> subjects = subjectJpaDao.findAllThatUserHasDone(user, DEFAULT_PAGE, DEFAULT_ORDER, DEFAULT_DIR);

        assertEquals(1, subjects.size());
        assertTrue(subjects.contains(subject));
    }

    @Rollback
    @Test
    public void testAddPrerequisites() {
        subjectJpaDao.addPrerequisites(subject, new ArrayList<>(Collections.singletonList(subject2.getId())));

        assertTrue(subject.getPrerequisites().contains(subject2));
    }

    @Rollback
    @Test(expected = SubjectNotFoundException.class)
    public void testAddInvalidPrerequisite() {
        subjectJpaDao.addPrerequisites(subject, new ArrayList<>(Collections.singleton("Invalid")));

        Assert.fail("SubjectNotFoundException must be thrown");
    }

    @Rollback
    @Test
    public void testAddClass() {
        final String classCode = "A";
        subjectJpaDao.addClassToSubject(subject, classCode);

        assertTrue(subject.getClassById(classCode).isPresent());
    }

    @Rollback
    @Test(expected = SubjectClassIdAlreadyExistsException.class)
    public void testAddClassThatAlreadyExists() {
        subjectJpaDao.addClassToSubject(subject2, "A");
        subjectJpaDao.addClassToSubject(subject2, "A");

        Assert.fail("SubjectClassIdAlreadyExistsException should be thrown.");
    }

    @Rollback
    @Test
    public void testEditSubject() {
        final String newName = "New Name";
        final String newDepartment = "Department";
        final Integer newCredits = 6;
        final Set<Subject> newPrerequisites = new HashSet<>(Collections.singletonList(subject2));

        final Subject newSub = subjectJpaDao.editSubject(subject4, newName, newDepartment, newCredits, newPrerequisites);

        assertEquals(newName, newSub.getName());
        assertEquals(newDepartment, newSub.getDepartment());
        assertEquals(newCredits, newSub.getCredits());
        assertEquals(newPrerequisites, newSub.getPrerequisites());
    }


    @Rollback
    @Test
    public void testAddClassTimes() {
        final SubjectClass subjectClass = subjectJpaDao.addClassToSubject(subject4, "A");
        final List<Integer> days = new ArrayList<>(Collections.singletonList(1));
        final List<LocalTime> startTimes = new ArrayList<>(Collections.singletonList(LocalTime.MIDNIGHT));
        final List<LocalTime> endTimes = new ArrayList<>(Collections.singletonList(LocalTime.NOON));
        final List<String> locations = new ArrayList<>(Collections.singletonList("201F"));
        final List<String> buildings = new ArrayList<>(Collections.singletonList("Financiero"));
        final List<String> modes = new ArrayList<>(Collections.singletonList("In Person"));

        final List<SubjectClassTime> classTimes = subjectJpaDao.addClassTimesToClass(subjectClass, days, startTimes, endTimes, locations, buildings, modes);

        assertEquals(1, classTimes.size());
        final SubjectClassTime newSubjectClassTime = classTimes.get(0);
        assertEquals(days.get(0).intValue(), newSubjectClassTime.getDay());
        assertEquals(startTimes.get(0), newSubjectClassTime.getStartTime());
        assertEquals(endTimes.get(0), newSubjectClassTime.getEndTime());
        assertEquals(locations.get(0), newSubjectClassTime.getClassLoc());
        assertEquals(buildings.get(0), newSubjectClassTime.getBuilding());
        assertEquals(modes.get(0), newSubjectClassTime.getMode());
    }
}
