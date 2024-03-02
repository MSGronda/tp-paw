package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.exceptions.SubjectClassNotFoundException;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.mock.DegreeMockData;
import ar.edu.itba.paw.persistence.mock.SubjectMockData;
import org.junit.After;
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
        jdbcTemplate.execute("ALTER SEQUENCE degrees_id_seq RESTART WITH 3;");
    }

    @Rollback
    @Test
    public void testFindById() {
        final Optional<Degree> degree = degreeJpaDao.findById(DegreeMockData.DEG1_ID);

        assertTrue(degree.isPresent());
        assertEquals(DegreeMockData.DEG1_ID, degree.get().getId());
        assertEquals(DegreeMockData.DEG1_NAME, degree.get().getName());
        assertEquals(DegreeMockData.DEG1_CREDITS, degree.get().getTotalCredits());
    }

    @Rollback
    @Test
    public void testFindByName() {
        final Optional<Degree> degree = degreeJpaDao.findByName(DegreeMockData.DEG1_NAME);

        assertTrue(degree.isPresent());
        assertEquals(DegreeMockData.DEG1_ID, degree.get().getId());
        assertEquals(DegreeMockData.DEG1_NAME, degree.get().getName());
        assertEquals(DegreeMockData.DEG1_CREDITS, degree.get().getTotalCredits());
    }

    @Rollback
    @Test
    public void testCreateDegree() {
        final Degree degreeToCreate = Degree.builder().name("Ing. Quimica").totalCredits(250).build();
        final Degree newDegree = degreeJpaDao.create(degreeToCreate);
        em.flush();

        assertEquals(degreeToCreate.getName(), newDegree.getName());
        assertEquals(degreeToCreate.getTotalCredits(), newDegree.getTotalCredits());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,"degrees","id = " + newDegree.getId()));
    }

    @Rollback
    @Test
    public void testEditName() {
        final String newName = "Ingenieria Mecanica";

        final Degree editedDegree = degreeJpaDao.editName(DegreeMockData.getDegree2(), newName);

        assertEquals(DegreeMockData.DEG2_ID, editedDegree.getId());
        assertEquals(newName, editedDegree.getName());
    }

    @Rollback
    @Test
    public void testEditTotalCredits() {
        final int newTotalCredits = 245;

        final Degree editedDegree = degreeJpaDao.editTotalCredits(DegreeMockData.getDegree2(), newTotalCredits);

        assertEquals(DegreeMockData.DEG2_ID, editedDegree.getId());
        assertEquals(newTotalCredits, editedDegree.getTotalCredits());
    }

    @Rollback
    @Test
    public void testAddSubjectToDegree() {
        final int semesterId = 1;
        final List<Long> degreeIds = new ArrayList<>(Collections.singletonList(DegreeMockData.DEG2_ID));
        final List<Integer> semesterIds = new ArrayList<>(Collections.singletonList(semesterId));
        final Subject subject = em.find(Subject.class, SubjectMockData.SUB1_ID);

        degreeJpaDao.addSubjectToDegrees(subject, degreeIds, semesterIds);
        em.flush();

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate,
                "subjectsdegrees",
                "iddeg = " + DegreeMockData.DEG2_ID + " AND semester = " + semesterId + " AND idsub = '" + SubjectMockData.SUB1_ID + "'"
        ));
    }
}


