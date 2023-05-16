package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ImageJdbcDaoTest {

    private static final long IMAGEID = 1;
    private static final long IMAGEID2 = 2;



    private static final File IMAGE = new File("src/test/resources/default_user.png");

    private static final File IMAGE2 = new File("src/test/resources/test_image.jpg");


    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    private DataSource ds;

    @Autowired
    private ImageDao imageDao;

    @Before
    public void setup(){
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("images").usingGeneratedKeyColumns("id");
    }

    @After
    public void clearDb(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "images");
    }

    @Test
    public void testFindById() throws IOException {
        final byte[] image = Files.readAllBytes(IMAGE.toPath());
        final Map<String, Object> args = new HashMap<>();
        args.put("image", image);
        long key = jdbcInsert.executeAndReturnKey(args).longValue();

        Optional<Image> maybeImage = imageDao.findById(key);

        Assert.assertTrue(maybeImage.isPresent());
        Assert.assertArrayEquals(image, maybeImage.get().getData());
    }

    @Test
    public void testGetAll() throws IOException {
        final byte[] image = Files.readAllBytes(IMAGE.toPath());
        final Map<String, Object> args = new HashMap<>();
        args.put("image", image);
        long key1 = jdbcInsert.executeAndReturnKey(args).longValue();

        final byte[] image2 = Files.readAllBytes(IMAGE2.toPath());
        final Map<String, Object> args2 = new HashMap<>();
        args2.put("image", image2);
        long key2 = jdbcInsert.executeAndReturnKey(args2).longValue();

        List<Image> list = imageDao.getAll();

        Assert.assertEquals(2, list.size());
        Assert.assertEquals(key1, list.get(0).getId());
        Assert.assertEquals(key2, list.get(1).getId());
        Assert.assertArrayEquals(image, list.get(0).getData());
        Assert.assertArrayEquals(image2, list.get(1).getData());

    }

    @Test
    public void testInsertImage() throws IOException {
        Image image = new Image(IMAGEID, Files.readAllBytes(IMAGE.toPath()));
        imageDao.insert(image);

        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "images"));
    }

    @Test
    public void testInsertByteArray() throws IOException {
        imageDao.insert(Files.readAllBytes(IMAGE.toPath()));

        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "images"));
    }

    @Test
    public void testInsertAndReturnKey() throws IOException {
        imageDao.insert(Files.readAllBytes(IMAGE.toPath()));

        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "images"));
    }

    @Test
    public void testDelete() throws IOException {
        final byte[] image = Files.readAllBytes(IMAGE.toPath());
        final Map<String, Object> args = new HashMap<>();
        args.put("image", image);
        long key1 = jdbcInsert.executeAndReturnKey(args).longValue();

        imageDao.delete(key1);

        Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "images"));
    }

    @Test
    public void testUpdate() throws IOException {
        final byte[] image = Files.readAllBytes(IMAGE.toPath());
        final Map<String, Object> args = new HashMap<>();
        args.put("image", image);
        long key1 = jdbcInsert.executeAndReturnKey(args).longValue();

        final byte[] imageToUpload = Files.readAllBytes(IMAGE2.toPath());
        Image imageUpdated = new Image(key1, imageToUpload);
        imageDao.update(imageUpdated);

        String query = "id = " + key1;
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "images", query));

        byte[] imageInDB = jdbcTemplate.queryForObject("SELECT image FROM images WHERE id = ?", byte[].class, key1);
        Assert.assertArrayEquals(imageToUpload, imageInDB );
    }

    @Test
    public void testUpdateImage() throws IOException {
        final byte[] image = Files.readAllBytes(IMAGE.toPath());
        final Map<String, Object> args = new HashMap<>();
        args.put("image", image);
        long key1 = jdbcInsert.executeAndReturnKey(args).longValue();

        final byte[] imageToUpload = Files.readAllBytes(IMAGE2.toPath());
        imageDao.updateImage(key1, imageToUpload);

        String query = "id = " + key1;
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "images", query));

        byte[] imageInDB = jdbcTemplate.queryForObject("SELECT image FROM images WHERE id = ?", byte[].class, key1);
        Assert.assertArrayEquals(imageToUpload, imageInDB );
    }
}
