package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.persistence.config.TestConfig;
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
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class DegreeJpaDaoTest {
//    private static final Subject testSubject = Subject.builder().id("11.15").name("Test Subject").department("Informatica").credits(6).build();

    private final Degree testDegree = Degree.builder().id(1).name("Ing. Informatica").totalCredits(240).build();
    private final Degree testDegree2 = Degree.builder().name("Ing. Quimica").totalCredits(250).build();
    private final Degree testDegree3 = Degree.builder().id(2).name("Ing. Mecanica").totalCredits(240).build();

    @Autowired
    private DataSource dataSource;
    @PersistenceContext
    private EntityManager em;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DegreeJpaDao degreeJpaDao;


    @Before
    public void setup() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Rollback
    @Test
    public void testFindById() {
        final Optional<Degree> degree = degreeJpaDao.findById(testDegree.getId());

        assertTrue(degree.isPresent());
        assertEquals(testDegree.getId(), degree.get().getId());
        assertEquals(testDegree.getName(), degree.get().getName());
        assertEquals(testDegree.getTotalCredits(), degree.get().getTotalCredits());
    }

    @Rollback
    @Test
    public void testFindByName() {
        final Optional<Degree> degree = degreeJpaDao.findByName(testDegree.getName());

        assertTrue(degree.isPresent());
        assertEquals(testDegree.getId(), degree.get().getId());
        assertEquals(testDegree.getName(), degree.get().getName());
        assertEquals(testDegree.getTotalCredits(), degree.get().getTotalCredits());
    }

    @Rollback
    @Test
    public void testCreateDegree() {
        final Degree newDegree = degreeJpaDao.create(testDegree2);

        assertEquals(testDegree2.getName(), newDegree.getName());
        assertEquals(testDegree2.getTotalCredits(), newDegree.getTotalCredits());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,"degrees","id = " + testDegree3.getId()));
    }

    @Rollback
    @Test
    public void testEditName() {
        final String newName = "Ingenieria Mecanica";

        final Degree editedDegree = degreeJpaDao.editName(testDegree3, newName);

        assertEquals(testDegree3.getId(), editedDegree.getId());
        assertEquals(newName, editedDegree.getName());
    }

    @Rollback
    @Test
    public void testEditTotalCredits() {
        final int newTotalCredits = 245;

        final Degree editedDegree = degreeJpaDao.editTotalCredits(testDegree3, newTotalCredits);

        assertEquals(testDegree3.getId(), editedDegree.getId());
        assertEquals(newTotalCredits, editedDegree.getTotalCredits());
    }

    @Rollback
    @Test
    public void testAddSubjectToDegree() {
//        final int semesterId = 1;
//        final List<Long> degreeIds = new ArrayList<>(Collections.singletonList(testDegree3.getId()));
//        final List<Integer> semesterIds = new ArrayList<>(Collections.singletonList(semesterId));
//
//        degreeJpaDao.addSubjectToDegrees(testSubject, degreeIds, semesterIds);
//
//        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(
//                jdbcTemplate,
//                "subjectsdegrees",
//                "iddeg = " + testDegree3.getId() + " AND semester = " + semesterId + " AND idsub = '" + testSubject.getId() + "'"
//        ));
    }
}


