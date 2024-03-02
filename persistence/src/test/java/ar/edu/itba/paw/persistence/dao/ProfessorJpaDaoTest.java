package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.mock.ProfessorMockData;
import ar.edu.itba.paw.persistence.mock.SubjectMockData;
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
        assertEquals(ProfessorMockData.PROF1_NAME, professor.get().getName());
    }

    @Rollback
    @Test
    public void testFindByName() {

        final Optional<Professor> professor = professorJpaDao.getByName(ProfessorMockData.PROF1_NAME);

        assertTrue(professor.isPresent());
        assertEquals(ProfessorMockData.PROF1_NAME, professor.get().getName());
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
        final Professor professorToCreate = new Professor("New Professor");

        final Professor newProfessor = professorJpaDao.create(professorToCreate);
        em.flush();

        assertEquals(professorToCreate.getName(), newProfessor.getName());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate,
                "professors",
                "profname = '" + professorToCreate.getName() + "'"
        ));
    }


    @Rollback
    @Test
    public void testAddSubjectToProfessors() {

        professorJpaDao.addSubjectToProfessors(SubjectMockData.getSubject1(), new ArrayList<>(Collections.singletonList(ProfessorMockData.PROF1_NAME)));
        em.flush();

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate,
                "professorssubjects",
                "idprof = " + 1 + " AND idsub = '" + SubjectMockData.SUB1_ID + "'"
        ));
    }


}
