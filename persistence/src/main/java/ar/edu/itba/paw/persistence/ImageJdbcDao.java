package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.persistence.constants.Tables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ImageJdbcDao implements ImageDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageJdbcDao.class);

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public ImageJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
            .withTableName(Tables.IMG)
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Image> findById(final Long aLong) {
        return jdbcTemplate.query("SELECT * FROM " + Tables.IMG + " WHERE id = ?", ImageJdbcDao::rowMapper, aLong)
            .stream().findFirst();
    }

    @Override
    public List<Image> getAll() {
        return jdbcTemplate.query("SELECT * FROM " + Tables.IMG, ImageJdbcDao::rowMapper);
    }

    @Override
    public void insert(final Image image) {
        insert(image.getData());
    }

    @Override
    public void insert(final byte[] image) {
        insertAndReturnKey(image);
    }

    @Override
    public Long insertAndReturnKey(final byte[] image) {
        Map<String,Object> args = new HashMap<>();
        args.put("image", image);

        long success = jdbcInsert.executeAndReturnKey(args).longValue();
        if(success != 0) {
            LOGGER.info("Image created for registered user");
        } else {
            LOGGER.warn("Image creation for registered user failed");
        }
        return success;
    }

    @Override
    public void delete(final Long aLong) {
        jdbcTemplate.execute("DELETE FROM " + Tables.IMG + " WHERE id = " + aLong);
        LOGGER.warn("Image with id {} has been deleted", aLong);
    }

    @Override
    public void update(final Image image) {
        updateImage(image.getId(), image.getData());
    }
    @Override
    public void updateImage(final long id, final byte[] image) {
        int success = jdbcTemplate.update("UPDATE " + Tables.IMG + " SET image = ? WHERE id = ?", image, id);
        if(success != 0) {
            LOGGER.info("Updated profile picture for current user");
        } else {
            LOGGER.warn("Profile picture update for current user failed");
        }
    }

    private static Image rowMapper(final ResultSet rs, final int rowNum) throws SQLException {
        return new Image(
            rs.getLong("id"),
            rs.getBytes("image")
        );
    }
}
