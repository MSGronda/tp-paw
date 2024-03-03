package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.mock.ImageMockData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ImageJpaDaoTest {
    @Autowired
    private DataSource dataSource;
    @PersistenceContext
    private EntityManager em;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ImageJpaDao imageJpaDao;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("ALTER SEQUENCE images_id_seq RESTART WITH 2;");
    }

    @Rollback
    @Test
    public void testFindById() {
        final Optional<Image> image = imageJpaDao.findById(ImageMockData.IMG1_ID);

        assertTrue(image.isPresent());
        assertArrayEquals(ImageMockData.IMG1_DATA, image.get().getImage());
    }

    @Rollback
    @Test
    public void testCreateImage() {
        final byte[] imageData = {1,2,3};

        final Image createdImage = imageJpaDao.create(imageData);
        em.flush();

        assertArrayEquals(imageData, createdImage.getImage());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "images", "id = " + createdImage.getId()));
    }

    @Rollback
    @Test
    public void testUpdateImage() {
        final byte[] newImageData = {1,2,3};
        final Image image = em.find(Image.class, ImageMockData.IMG1_ID);

        final Image updatedImage = imageJpaDao.update(image, newImageData);

        assertArrayEquals(newImageData, updatedImage.getImage());
    }

}
