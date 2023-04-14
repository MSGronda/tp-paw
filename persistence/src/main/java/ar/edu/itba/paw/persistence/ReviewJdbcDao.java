package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
public class ReviewJdbcDao implements ReviewDao {
    private static final RowMapper<Review> ROW_MAPPER = ReviewJdbcDao::rowMapper;

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final String review_table_name = "reviews";

    @Autowired
    public ReviewJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("reviews")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Review> findById(Long id) {
        return jdbcTemplate.query("SELECT * FROM " +review_table_name+ " WHERE id = ?",ROW_MAPPER,id)
                .stream().findFirst();
    }

    @Override
    public List<Review> getAll() {
        return jdbcTemplate.query("SELECT * FROM "+review_table_name, ROW_MAPPER);
    }

    @Override
    public void insert(Review review) {
        create(review.getEasy(), review.getTimeDemanding(), review.getText(), review.getSubjectId(),review.getUserId());
    }

    @Override
    public Review create(Boolean easy, Boolean timeDemanding, String text,String subjectId,long userId) {
        Map<String, Object> data = new HashMap<>();
        data.put("easy", easy);
        data.put("timeDemanding", timeDemanding);
        data.put("revText", text);
        data.put("idSub", subjectId);
        data.put("idUser", userId);

        Number key = jdbcInsert.executeAndReturnKey(data);

        return new Review(key.longValue(), userId, subjectId, easy, timeDemanding, text);
    }

    @Override
    public void delete(Long integer) {

    }

    @Override
    public void update(Review review) {

    }

    private static Review rowMapper(ResultSet rs, int rowNum) throws SQLException {
        return new Review(
                rs.getLong("id"),
                rs.getLong("idUser"),
                rs.getString("idSub"),
                rs.getBoolean("easy"),
                rs.getBoolean("timeDemanding"),
                rs.getString("revText")
        );
    }
}
