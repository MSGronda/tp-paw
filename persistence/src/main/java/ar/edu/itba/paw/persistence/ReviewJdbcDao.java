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
import java.util.*;

@Repository
public class ReviewJdbcDao implements ReviewDao {
    private static final RowMapper<Review> ROW_MAPPER = ReviewJdbcDao::rowMapper;
    private static final RowMapper<Integer> DIFFICULTY_ROW_MAPPER = ReviewJdbcDao::difficultyRowMapper;
    private static final RowMapper<Integer> TIME_ROW_MAPPER = ReviewJdbcDao::timeRowMapper;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final String TABLE_REVIEWS = "reviews";
    private final String TABLE_SUB = "subjects";

    @Autowired
    public ReviewJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("reviews")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Review> findById(Long id) {
        return jdbcTemplate.query("SELECT * FROM " + TABLE_REVIEWS + " WHERE id = ?",ROW_MAPPER,id)
                .stream().findFirst();
    }

    @Override
    public List<Review> getAllBySubject(String idsub) {
        return jdbcTemplate.query("SELECT * FROM " + TABLE_REVIEWS + " WHERE idsub = ?", ROW_MAPPER, idsub );
    }
    @Override
    public Optional<Integer> getDifficultyBySubject(String idsub) {
        return jdbcTemplate.query("SELECT easy FROM " + TABLE_REVIEWS + " WHERE idSub = ? GROUP BY easy ORDER BY COUNT(*) desc, easy desc", DIFFICULTY_ROW_MAPPER, idsub)
                .stream().findFirst();
    }
    @Override
    public Optional<Integer> getTimeBySubject(String idsub) {
        return jdbcTemplate.query("SELECT timedemanding FROM " + TABLE_REVIEWS + " WHERE idSub = ? GROUP BY timedemanding ORDER BY COUNT(*) desc, timedemanding desc", TIME_ROW_MAPPER, idsub)
                .stream().findFirst();
    }

    @Override
    public List<Review> getAll() {
        return jdbcTemplate.query("SELECT * FROM " + TABLE_REVIEWS, ROW_MAPPER);
    }

    @Override
    public void insert(Review review) {
        create(review.getEasy(), review.getTimeDemanding(), review.getText(), review.getSubjectId(),review.getUserId(), review.getUserEmail());
    }

    @Override
    public Review create(Integer easy, Integer timeDemanding, String text,String subjectId,long userId, String userEmail) {
        Map<String, Object> data = new HashMap<>();
        data.put("easy", easy);
        data.put("timeDemanding", timeDemanding);
        data.put("revText", text);
        data.put("idSub", subjectId);
        data.put("idUser", userId);
        data.put("userEmail", userEmail);

        Number key = jdbcInsert.executeAndReturnKey(data);

        return new Review(key.longValue(), userId, userEmail, subjectId, easy, timeDemanding, text);
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
                rs.getString("userEmail"),
                rs.getString("idSub"),
                rs.getInt("easy"),
                rs.getInt("timeDemanding"),
                rs.getString("revText")
        );
    }
    private static Integer difficultyRowMapper(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("easy");
    }
    private static Integer timeRowMapper(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("timedemanding");
    }

   //-----------------------------------------------------------------
    @Override
    public List<Review> getAllUserReviewsWithSubjectName(Long userId) {
        return jdbcTemplate.query("SELECT * FROM " + TABLE_SUB +" FULL JOIN " + TABLE_REVIEWS + " ON " + TABLE_SUB +".id = " + TABLE_REVIEWS + ".idsub WHERE iduser = ?", ReviewJdbcDao::subjectNameRowMapper, userId);
    }

    private static Review subjectNameRowMapper(ResultSet rs, int rowNum) throws SQLException {
        return new Review(
                rs.getLong("id"),
                rs.getLong("idUser"),
                rs.getString("userEmail"),
                rs.getString("idSub"),
                rs.getInt("easy"),
                rs.getInt("timeDemanding"),
                rs.getString("revText"),
                rs.getString("subname")
        );
    }
}
