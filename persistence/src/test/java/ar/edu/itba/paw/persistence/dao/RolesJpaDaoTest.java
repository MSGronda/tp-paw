package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.mock.RoleMockData;
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

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class RolesJpaDaoTest {

    @Autowired
    private DataSource dataSource;
    @PersistenceContext
    private EntityManager em;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RolesJpaDao rolesJpaDao;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Rollback
    @Test
    public void testFindById() {
        final Optional<Role> role = rolesJpaDao.findById(RoleMockData.USER_ROLE_ID);

        assertTrue(role.isPresent());
        assertEquals(RoleMockData.USER_ROLE_ID, role.get().getId());
        assertEquals(RoleMockData.USER_ROLE_NAME, role.get().getName());
    }

    @Rollback
    @Test
    public void testFindByName() {
        final Optional<Role> role = rolesJpaDao.findByName(RoleMockData.EDITOR_ROLE_NAME);

        assertTrue(role.isPresent());
        assertEquals(RoleMockData.EDITOR_ROLE_ID, role.get().getId());
        assertEquals(RoleMockData.EDITOR_ROLE_NAME, role.get().getName());
    }

    @Rollback
    @Test
    public void testGetAll() {
        final List<Role> roles = rolesJpaDao.getAll();

        assertEquals(2, roles.size());
        assertTrue(roles.contains(RoleMockData.getUserRole()));
        assertTrue(roles.contains(RoleMockData.getEditorRole()));
    }

}
