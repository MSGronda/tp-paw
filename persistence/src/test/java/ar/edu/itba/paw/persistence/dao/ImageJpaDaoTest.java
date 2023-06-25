package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Image;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ImageJpaDaoTest {
    private static final byte[] IMAGE = new byte[]{1, 2, 3, 4, 5};
    private static final byte[] IMAGE2 = new byte[]{5, 4, 3, 2, 1};

    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";
    private final static String EMAIL = "e@mail.com";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ImageJpaDao imageJpaDao;

    @Before
    public void clear() {
        em.createQuery("DELETE FROM Image").executeUpdate();
    }

    @Test
    public void create() {
        final Image image = imageJpaDao.create(IMAGE);

        assertNotNull(image);
        assertEquals(IMAGE, image.getImage());
        assertEquals(image, em.find(Image.class, image.getId()));
    }

    @Test
    public void update() {
        final Image image = new Image(IMAGE);
        em.persist(image);

        imageJpaDao.update(image, IMAGE2);

        assertEquals(IMAGE2, image.getImage());
    }

    @Test
    public void findById() {
        final Image image = new Image(IMAGE);
        em.persist(image);

        final Image actual = imageJpaDao.findById(image.getId()).get();
        assertEquals(image.getId(), actual.getId());
        assertEquals(image.getImage(), actual.getImage());
    }
}
