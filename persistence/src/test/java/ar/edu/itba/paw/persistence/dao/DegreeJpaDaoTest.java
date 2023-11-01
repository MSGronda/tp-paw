package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.DegreeSubject;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.*;

import static org.junit.Assert.*;
/*
@SuppressWarnings("OptionalGetWithoutIsPresent")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class DegreeJpaDaoTest {
    private final static String DEGREE_NAME = "degree";
    private final static String DEGREE2_NAME = "degree2";

    private final static String SUB_ID = "id";
    private final static String SUB_NAME = "sub";
    private final static String SUB_DEPARTMENT = "dep";
    private final static int SUB_CREDITS = 3;


    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DegreeJpaDao degreeJpaDao;

    @Before
    public void clear() {
        em.createQuery("DELETE FROM Subject").executeUpdate();
        em.createQuery("DELETE FROM Degree").executeUpdate();
        em.createQuery("DELETE FROM DegreeSubject").executeUpdate();
    }

    @Test
    public void create() {
        final Degree deg = degreeJpaDao.create(DEGREE_NAME);

        assertNotNull(deg);
        assertEquals(DEGREE_NAME, deg.getName());
    }

    @Test
    public void findById() {
        final Degree deg = new Degree(DEGREE_NAME);
        em.persist(deg);

        assertEquals(deg, degreeJpaDao.findById(deg.getId()).get());
    }

    @Test
    public void findByName() {
        final Degree deg = new Degree(DEGREE_NAME);
        em.persist(deg);

        assertEquals(deg, degreeJpaDao.findByName(DEGREE_NAME).get());
    }

    @Test
    public void getAll() {
        final Degree deg1 = new Degree(DEGREE_NAME);
        final Degree deg2 = new Degree(DEGREE2_NAME);
        em.persist(deg1);
        em.persist(deg2);

        final List<Degree> degs = degreeJpaDao.getAll();

        assertEquals(2, degs.size());
        assertTrue(degs.contains(deg1));
        assertTrue(degs.contains(deg2));
    }

    @Test
    public void findSubjectSemesterForDegree() {
        final Degree deg = new Degree(DEGREE_NAME);
        em.persist(deg);

        final Subject sub = Subject.builder()
                .id(SUB_ID)
                .name(SUB_NAME)
                .credits(SUB_CREDITS)
                .department(SUB_DEPARTMENT)
                .build();

        em.persist(sub);

        final DegreeSubject degSub = new DegreeSubject(deg, sub, 1);
        em.persist(degSub);

        assertEquals(OptionalInt.of(1), degreeJpaDao.findSubjectSemesterForDegree(sub, deg));
    }

    @Test
    public void addSubjectToDegrees() {
        final Degree deg = new Degree(DEGREE_NAME);
        em.persist(deg);
        final Degree deg2 = new Degree(DEGREE2_NAME);
        em.persist(deg2);

        final Subject sub = Subject.builder()
                .id(SUB_ID)
                .name(SUB_NAME)
                .credits(SUB_CREDITS)
                .department(SUB_DEPARTMENT)
                .build();

        em.persist(sub);
        final List<Long> degreeIds = new ArrayList<>();
        degreeIds.add(deg.getId());
        degreeIds.add(deg2.getId());
        final List<Integer> semesters = new ArrayList<>();
        semesters.add(1);
        semesters.add(4);

        degreeJpaDao.addSubjectToDegrees(sub, degreeIds, semesters);

        assertEquals(1, deg.getDegreeSubjects().size());
        assertEquals(1, deg2.getDegreeSubjects().size());

        assertEquals(sub, deg.getDegreeSubjects().get(0).getSubject());
        assertEquals(sub, deg2.getDegreeSubjects().get(0).getSubject());

        assertEquals(1, deg.getDegreeSubjects().get(0).getSemester());
        assertEquals(4, deg2.getDegreeSubjects().get(0).getSemester());

        assertEquals(deg, deg.getDegreeSubjects().get(0).getDegree());
        assertEquals(deg2, deg2.getDegreeSubjects().get(0).getDegree());

        assertEquals(sub, deg.getDegreeSubjects().get(0).getSubject());
        assertEquals(sub, deg2.getDegreeSubjects().get(0).getSubject());
    }

    @Test
    public void updateInsertSubjectToDegrees() {
        final Degree deg = new Degree(DEGREE_NAME);
        em.persist(deg);
        final Degree deg2 = new Degree(DEGREE2_NAME);
        em.persist(deg2);

        final Subject sub = Subject.builder()
                .id(SUB_ID)
                .name(SUB_NAME)
                .credits(SUB_CREDITS)
                .department(SUB_DEPARTMENT)
                .build();

        em.persist(sub);
        final List<Degree> degreeIds = new ArrayList<>();
        degreeIds.add(deg);
        degreeIds.add(deg2);
        final List<Integer> semesters = new ArrayList<>();
        semesters.add(1);
        semesters.add(4);

        degreeJpaDao.updateInsertSubjectToDegrees(sub, degreeIds, semesters);

        assertEquals(1, deg.getDegreeSubjects().size());
        assertEquals(1, deg2.getDegreeSubjects().size());

        assertEquals(sub, deg.getDegreeSubjects().get(0).getSubject());
        assertEquals(sub, deg2.getDegreeSubjects().get(0).getSubject());

        assertEquals(1, deg.getDegreeSubjects().get(0).getSemester());
        assertEquals(4, deg2.getDegreeSubjects().get(0).getSemester());

        assertEquals(deg, deg.getDegreeSubjects().get(0).getDegree());
        assertEquals(deg2, deg2.getDegreeSubjects().get(0).getDegree());

        assertEquals(sub, deg.getDegreeSubjects().get(0).getSubject());
        assertEquals(sub, deg2.getDegreeSubjects().get(0).getSubject());

    }

    @Test
    public void updateUpdateSubjectToDegrees() {
        final Degree deg = new Degree(DEGREE_NAME);
        em.persist(deg);
        final Degree deg2 = new Degree(DEGREE2_NAME);
        em.persist(deg2);

        final Subject sub = Subject.builder()
                .id(SUB_ID)
                .name(SUB_NAME)
                .credits(SUB_CREDITS)
                .department(SUB_DEPARTMENT)
                .build();

        em.persist(sub);

        final List<DegreeSubject> degSubjects = new ArrayList<>();
        final DegreeSubject degSub = new DegreeSubject(deg, sub, 1);
        final DegreeSubject degSub2 = new DegreeSubject(deg2, sub, 5);
        degSubjects.add(degSub);
        degSubjects.add(degSub2);

        final List<Integer> semesters = new ArrayList<>();
        semesters.add(2);
        semesters.add(3);

        degreeJpaDao.updateUpdateSubjectToDegrees(sub, degSubjects, semesters);

        assertEquals(2, degSub.getSemester());
        assertEquals(3, degSub2.getSemester());
    }

    @Test
    public void updateDeleteSubjectToDegrees() {
        final Degree deg = new Degree(DEGREE_NAME);
        em.persist(deg);
        final Degree deg2 = new Degree(DEGREE2_NAME);
        em.persist(deg2);

        final Subject sub = Subject.builder()
                .id(SUB_ID)
                .name(SUB_NAME)
                .credits(SUB_CREDITS)
                .department(SUB_DEPARTMENT)
                .build();

        em.persist(sub);

        final List<DegreeSubject> degSubjects = new ArrayList<>();
        final DegreeSubject degSub = new DegreeSubject(deg, sub, 1);
        final DegreeSubject degSub2 = new DegreeSubject(deg2, sub, 5);
        degSubjects.add(degSub);
        degSubjects.add(degSub2);

        degreeJpaDao.updateDeleteSubjectToDegrees(sub, degSubjects);

        assertEquals(0, deg.getDegreeSubjects().size());
        assertEquals(0, deg2.getDegreeSubjects().size());
    }
}


 */