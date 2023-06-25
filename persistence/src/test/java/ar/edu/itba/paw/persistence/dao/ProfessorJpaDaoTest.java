package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Professor;
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
public class ProfessorJpaDaoTest {
    private final static String NAME = "name";
    private final static String NAME_2 = "name2";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ProfessorJpaDao professorJpaDao;

    @Before
    public void clear() {
        em.createQuery("DELETE FROM Professor").executeUpdate();
    }

    @Test
    public void findById() {
        final Professor professor = new Professor(NAME);
        em.persist(professor);

        final Professor actual = professorJpaDao.findById(professor.getId()).get();
        assertEquals(professor.getId(), actual.getId());
        assertEquals(professor.getName(), actual.getName());
    }

    @Test
    public void getAll() {
        final Professor professor = new Professor(NAME);
        final Professor professor2 = new Professor(NAME_2);
        em.persist(professor);
        em.persist(professor2);

        final List<Professor> actual = professorJpaDao.getAll();
        assertEquals(2, actual.size());
        assertTrue(actual.contains(professor));
        assertTrue(actual.contains(professor2));
    }
}
