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

    @Test
    public void updateSubjectToProfessorsAdd(){
        final List<Professor> professorList = new ArrayList<>();
        final Subject sub = Subject.builder().id(SUBJECT_CODE).name(SUBJECT_NAME).credits(9).department(DEPARTMENT_NAME).build();
        final Professor professor = new Professor(NAME);
        final Professor professor2 = new Professor(NAME_2);

        em.persist(sub);
        em.persist(professor);
        em.persist(professor2);

        professorList.add(professor);
        professorList.add(professor2);

        professorJpaDao.updateSubjectToProfessorsAdd(sub,professorList);

        Optional<Subject> maybeSubejct = em.createQuery("from Subject as s where s.id = :id")
                .setParameter("id",sub.getId())
                .getResultList().stream().findFirst();
        assertTrue(maybeSubejct.isPresent());
        Set<Professor> pS = maybeSubejct.get().getProfessors();
        Professor[] pA = {professor,professor2};
        assertArrayEquals(pS.toArray(),pA);

    }

    @Test
    public void updateSubjectToProfessorsCreate(){
        final List<String> professorList = new ArrayList<>();
        final Subject sub = Subject.builder().id(SUBJECT_CODE).name(SUBJECT_NAME).credits(9).department(DEPARTMENT_NAME).build();
        final Professor professor = new Professor(NAME);
        final Professor professor2 = new Professor(NAME_2);

        em.persist(sub);

        professorList.add(NAME);
        professorList.add(NAME_2);

        professorJpaDao.updateSubjectToProfessorsCreate(sub,professorList);

        Optional<Professor> maybeProfessor = em.createQuery("from Professor as p where p.name = :name", Professor.class)
                .setParameter("name",NAME).getResultList().stream().findFirst();
        Optional<Professor> maybeProfessor2 = em.createQuery("from Professor as p where p.name = :name", Professor.class)
                .setParameter("name",NAME_2).getResultList().stream().findFirst();

        assertTrue(maybeProfessor.isPresent());
        assertTrue(maybeProfessor2.isPresent());

        Optional<Subject> maybeSubejct = em.createQuery("from Subject as s where s.id = :id")
                .setParameter("id",sub.getId())
                .getResultList().stream().findFirst();

        assertTrue(maybeSubejct.isPresent());
        Set<Professor> pS = maybeSubejct.get().getProfessors();
        assertTrue(pS.contains(maybeProfessor.get()));
        assertTrue(pS.contains(maybeProfessor2.get()));
    }

    @Test
    public void updateSubjectToProfessorsRemove(){
        final List<Professor> professorList = new ArrayList<>();
        final Subject sub = Subject.builder().id(SUBJECT_CODE).name(SUBJECT_NAME).credits(9).department(DEPARTMENT_NAME).build();
        final Professor professor = new Professor(NAME);
        final Professor professor2 = new Professor(NAME_2);

        em.persist(sub);
        em.persist(professor);
        em.persist(professor2);

        professorList.add(professor);
        professorList.add(professor2);

        professorJpaDao.updateSubjectToProfessorsRemove(sub,professorList);

        Optional<Subject> maybeSubejct = em.createQuery("from Subject as s where s.id = :id")
                .setParameter("id",sub.getId())
                .getResultList().stream().findFirst();
        assertTrue(maybeSubejct.isPresent());
        Set<Professor> pS = maybeSubejct.get().getProfessors();
        assertFalse(pS.contains(professor));
        assertFalse(pS.contains(professor2));
    }

    @Test
    public void addProfessorsToClasses(){
        final Subject sub = Subject.builder().id(SUBJECT_CODE).name(SUBJECT_NAME).credits(9).department(DEPARTMENT_NAME).build();
        final SubjectClass sC = new SubjectClass(CLASS_ID,sub);
        final SubjectClass sC2 = new SubjectClass(CLASS_ID2,sub);
        final List<List<String>> classProfessors = new ArrayList<>();
        classProfessors.add(new ArrayList<>());
        classProfessors.add(new ArrayList<>());
        classProfessors.get(0).add(NAME);
        classProfessors.get(0).add(NAME_2);
        classProfessors.get(1).add(NAME);
        final List<String> classCodes = new ArrayList<>();
        classCodes.add(CLASS_ID);
        classCodes.add(CLASS_ID2);
        final Professor professor = new Professor(NAME);
        final Professor professor2 = new Professor(NAME_2);
        sub.getClasses().add(sC);
        sub.getClasses().add(sC2);

        em.persist(sub);
        em.persist(sC);
        em.persist(sC2);
        em.persist(professor);
        em.persist(professor2);

        professorJpaDao.addProfessorsToClasses(sub,classCodes,classProfessors);

        assertTrue(sC.getProfessors().contains(professor));
        assertTrue(sC.getProfessors().contains(professor2));
        assertTrue(sC2.getProfessors().contains(professor));
    }

    @Test
    public void updateProfessorsToClassesAdd() {
        final Map<SubjectClass,List<Professor>> professorsToAdd = new HashMap<>();
        final Subject sub = Subject.builder().id(SUBJECT_CODE).name(SUBJECT_NAME).credits(9).department(DEPARTMENT_NAME).build();
        final SubjectClass sC = new SubjectClass(CLASS_ID,sub);
        final SubjectClass sC2 = new SubjectClass(CLASS_ID2,sub);
        final Professor professor = new Professor(NAME);
        final Professor professor2 = new Professor(NAME_2);
        final List<Professor> profList = new ArrayList<>();
        profList.add(professor);
        profList.add(professor2);
        professorsToAdd.put(sC,profList);
        professorsToAdd.put(sC2,new ArrayList<>());

        em.persist(sub);
        em.persist(sC);
        em.persist(sC2);
        em.persist(professor);
        em.persist(professor2);

        professorJpaDao.updateProfessorsToClassesAdd(professorsToAdd);

        assertTrue(sC.getProfessors().contains(professor));
        assertTrue(sC.getProfessors().contains(professor2));
        assertTrue(sC2.getProfessors().isEmpty());

    }

    @Test
    public void updateProfessorsToClassesRemove() {
        final Map<SubjectClass,List<Professor>> professorsToRemove = new HashMap<>();
        final Subject sub = Subject.builder().id(SUBJECT_CODE).name(SUBJECT_NAME).credits(9).department(DEPARTMENT_NAME).build();
        final SubjectClass sC = new SubjectClass(CLASS_ID,sub);
        final SubjectClass sC2 = new SubjectClass(CLASS_ID2,sub);
        final Professor professor = new Professor(NAME);
        final Professor professor2 = new Professor(NAME_2);
        final List<Professor> profList = new ArrayList<>();
        final List<Professor> profList2 = new ArrayList<>();
        profList2.add(professor2);
        profList.add(professor);
        profList.add(professor2);
        sC.getProfessors().addAll(profList);
        sC2.getProfessors().add(professor2);
        professorsToRemove.put(sC,profList2);
        professorsToRemove.put(sC2,profList2);

        em.persist(sub);
        em.persist(sC);
        em.persist(sC2);
        em.persist(professor);
        em.persist(professor2);

        professorJpaDao.updateProfessorsToClassesRemove(professorsToRemove);

        assertEquals(sC.getProfessors().get(0),professor);
        assertTrue(sC2.getProfessors().isEmpty());
    }
}
