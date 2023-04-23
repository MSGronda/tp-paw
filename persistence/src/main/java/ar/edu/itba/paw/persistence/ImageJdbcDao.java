package ar.edu.itba.paw.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@Repository
public class ImageJdbcDao implements ImageDao{

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final static String profileImage_table_name = "profileImage";


    @Autowired
    public ImageJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(profileImage_table_name)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public byte[] updateProfilePicture(long id, byte[] image) {
        if(hasProfilePicture(id))
            jdbcTemplate.update("UPDATE profileimages SET image = ? WHERE userid = ?", image, id);
        else
            crateProfilePicture(id, image);
        return image;
    }

    @Override
    public Optional<byte[]> getProfilePicture(long id) {
        return jdbcTemplate.query("SELECT * FROM "+ profileImage_table_name + "WHERE userId = ?", new Object[] { id }, ImageJdbcDao::rowMapper).stream().findFirst();
    }

    private boolean hasProfilePicture(long id) {
        return jdbcTemplate.queryForObject("SELECT EXISTS(SELECT * FROM "+ profileImage_table_name + "  WHERE id = ?)", new Object[] {id}, Boolean.class);
    }

    private void crateProfilePicture(long id, byte[] image) {
        final Map<String, Object> imageData = new HashMap<>();
        imageData.put("id", id);
        imageData.put("image", image);
        jdbcInsert.execute(imageData);
    }

    private static byte[] rowMapper(ResultSet rs, int rowNum) throws SQLException {
        return rs.getBytes("image");
    }

}
