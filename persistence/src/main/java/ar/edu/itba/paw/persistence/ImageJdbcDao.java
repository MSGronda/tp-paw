package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Image;
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
    private static final String TABLE = "images";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public ImageJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
            .withTableName(TABLE)
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Image> findById(Long aLong) {
        return jdbcTemplate.query("SELECT * FROM " + TABLE + " WHERE id = ?", ImageJdbcDao::rowMapper, aLong)
            .stream().findFirst();
    }

    @Override
    public List<Image> getAll() {
        return jdbcTemplate.query("SELECT * FROM " + TABLE, ImageJdbcDao::rowMapper);
    }

    @Override
    public void insert(Image image) {
        insert(image.getData());
    }

    @Override
    public void insert(byte[] image) {
        insertAndReturnKey(image);
    }

    @Override
    public Long insertAndReturnKey(byte[] image) {
        Map<String,Object> args = new HashMap<>();
        args.put("image", image);

        return jdbcInsert.executeAndReturnKey(args).longValue();
    }

    @Override
    public void delete(Long aLong) {
        jdbcTemplate.execute("DELETE FROM " + TABLE + " WHERE id = " + aLong);
    }

    @Override
    public void update(Image image) {
        jdbcTemplate.update("UPDATE " + TABLE + " SET image = ? WHERE id = ?", image.getData(), image.getId());
    }

    private static Image rowMapper(ResultSet rs, int rowNum) throws SQLException {
        return new Image(
            rs.getLong("id"),
            rs.getBytes("image")
        );
    }
}
