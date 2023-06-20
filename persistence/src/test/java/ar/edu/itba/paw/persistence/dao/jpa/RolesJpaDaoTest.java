package ar.edu.itba.paw.persistence.dao.jpa;

import ar.edu.itba.paw.models.Role;
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

import static org.junit.Assert.*;

@SuppressWarnings("ALL")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class RolesJpaDaoTest {
    private final static long ID_1 = 1L;
    private final static String NAME_1 = "name";
    private final static long ID_2 = 2L;
    private final static String NAME_2 = "name2";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private RolesJpaDao rolesJpaDao;

    @Before
    public void clear() {
        em.createQuery("DELETE FROM Role").executeUpdate();
    }

    @Test
    public void findById() {
        final Role role = new Role(ID_1,NAME_1);
        em.persist(role);

        final Role actual = rolesJpaDao.findById(role.getId()).get();

        assertNotNull(actual);
        assertEquals(NAME_1, actual.getName());
        assertEquals(ID_1, actual.getId());
    }

    @Test
    public void findByName() {
        final Role role = new Role(ID_1,NAME_1);
        em.persist(role);

        final Role actual = rolesJpaDao.findByName(NAME_1).get();

        assertNotNull(actual);
        assertEquals(NAME_1, actual.getName());
        assertEquals(ID_1, actual.getId());
    }

    @Test
    public void getAll() {
        final Role role1 = new Role(ID_1,NAME_1);
        final Role role2 = new Role(ID_2,NAME_2);
        em.persist(role1);
        em.persist(role2);

        final List<Role> actual = rolesJpaDao.getAll();

        assertEquals(2, actual.size());
        assertTrue(actual.contains(role1));
        assertTrue(actual.contains(role2));
    }
}
