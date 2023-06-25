package ar.edu.itba.paw.persistence.dao;

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

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class SubjectJpaDaoTest {

    private final static String ID = "75.06";
    private final static String NAME = "subject";
    private final static String DEPARTMENT = "department";
    private final static int CREDITS = 6;

    private final static String ID2 = "75.07";
    private final static String NAME2 = "subject2";
    private final static String DEPARTMENT2 = "department2";
    private final static int CREDITS2 = 3;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private SubjectJpaDao subjectJpaDao;

    @Before
    public void clean() {
        em.createQuery("DELETE FROM Subject").executeUpdate();
    }

    @Test
    public void findById() {
        final Subject subject = Subject.builder()
                .id(ID)
                .name(NAME)
                .department(DEPARTMENT)
                .credits(CREDITS)
                .build();

        em.persist(subject);

        final Subject actual = subjectJpaDao.findById(ID).get();

        assertEquals(subject.getId(), actual.getId());
        assertEquals(subject.getName(), actual.getName());
        assertEquals(subject.getDepartment(), actual.getDepartment());
        assertEquals(subject.getCredits(), actual.getCredits());
    }

    @Test
    public void getAll() {
        final Subject subject = Subject.builder()
                .id(ID)
                .name(NAME)
                .department(DEPARTMENT)
                .credits(CREDITS)
                .build();

        em.persist(subject);

        final Subject subject2 = Subject.builder()
                .id(ID2)
                .name(NAME2)
                .department(DEPARTMENT2)
                .credits(CREDITS2)
                .build();

        em.persist(subject2);

        final List<Subject> actual = subjectJpaDao.getAll();

        assertEquals(2, actual.size());
        assertTrue(actual.contains(subject));
        assertTrue(actual.contains(subject2));
    }
}
