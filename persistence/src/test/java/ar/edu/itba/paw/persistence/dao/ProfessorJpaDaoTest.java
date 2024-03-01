package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.ProfileValueSource;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ProfessorJpaDaoTest {

    private final Subject testSubject = Subject.builder().id("11.15").build();

    private final Professor testProfessor1 = new Professor( "Paula Daurat");
    private final Professor testProfessor2 = new Professor("New Professor");

    @Autowired
    private DataSource dataSource;
    @PersistenceContext
    private EntityManager em;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProfessorJpaDao professorJpaDao;

    @Before
    public void setup() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("ALTER SEQUENCE professors_id_seq RESTART WITH 2;");
    }

    @Rollback
    @Test
    public void testFindById() {

        final Optional<Professor> professor = professorJpaDao.findById(1);

        assertTrue(professor.isPresent());
        assertEquals(testProfessor1.getName(), professor.get().getName());
    }

    @Rollback
    @Test
    public void testFindByName() {

        final Optional<Professor> professor = professorJpaDao.getByName(testProfessor1.getName());

        assertTrue(professor.isPresent());
        assertEquals(testProfessor1.getName(), professor.get().getName());
    }

    @Rollback
    @Test
    public void testGetAll() {

        final List<Professor> professors = professorJpaDao.getAll();

        assertEquals(1, professors.size());
    }

    @Rollback
    @Test
    public void testCreateProfessor() {

        final Professor newProfessor = professorJpaDao.create(testProfessor2);
        em.flush();

        assertEquals(testProfessor2.getName(), newProfessor.getName());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate,
                "professors",
                "profname = '" + testProfessor2.getName() + "'"
        ));
    }


    @Rollback
    @Test
    public void testAddSubjectToProfessors() {

        professorJpaDao.addSubjectToProfessors(testSubject, new ArrayList<>(Collections.singletonList(testProfessor1.getName())));
        em.flush();

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate,
                "professorssubjects",
                "idprof = " + 1 + " AND idsub = '" + testSubject.getId() + "'"
        ));
    }


}
