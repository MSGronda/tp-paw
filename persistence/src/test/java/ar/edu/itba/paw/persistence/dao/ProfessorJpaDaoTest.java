package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.SubjectClass;
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

@SuppressWarnings("OptionalGetWithoutIsPresent")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ProfessorJpaDaoTest {
    private final static String NAME = "name";
    private final static String NAME_2 = "name2";
    private final static String SUBJECT_CODE = "10.01";
    private final static String SUBJECT_NAME = "Informatica General";
    private final static String DEPARTMENT_NAME = "Informatica";
    private final static String CLASS_ID = "A";
    private final static String CLASS_ID2 = "S";

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

    @Test
    public void addSubjectToProfessors(){
        final List<String> professorList = new ArrayList<>();
        final Professor professor = new Professor(NAME);
        final Professor professor2 = new Professor(NAME_2);
        final Subject sub = Subject.builder().id(SUBJECT_CODE).name(SUBJECT_NAME).credits(9).department(DEPARTMENT_NAME).build();

        professorList.add(professor.getName());
        professorList.add(professor2.getName());
        em.persist(professor);
        em.persist(professor2);

        professorJpaDao.addSubjectToProfessors(sub,professorList);

        assertEquals(professor.getSubjects().get(0),sub);
        assertEquals(professor2.getSubjects().get(0),sub);
    }

    @Test
    public void addSubjectToProfessors2(){
        final List<String> professorList = new ArrayList<>();
        final Subject sub = Subject.builder().id(SUBJECT_CODE).name(SUBJECT_NAME).credits(9).department(DEPARTMENT_NAME).build();

        em.persist(sub);

        professorList.add(NAME);
        professorList.add(NAME_2);

        professorJpaDao.addSubjectToProfessors(sub,professorList);

        Optional<Professor> maybeProfessor = em.createQuery("from Professor as p where p.name = :name", Professor.class)
                .setParameter("name",NAME).getResultList().stream().findFirst();
        Optional<Professor> maybeProfessor2 = em.createQuery("from Professor as p where p.name = :name", Professor.class)
                .setParameter("name",NAME_2).getResultList().stream().findFirst();

        assertTrue(maybeProfessor.isPresent());
        assertTrue(maybeProfessor2.isPresent());

        assertEquals(maybeProfessor.get().getSubjects().get(0),sub);
        assertEquals(maybeProfessor2.get().getSubjects().get(0),sub);
    }
}
