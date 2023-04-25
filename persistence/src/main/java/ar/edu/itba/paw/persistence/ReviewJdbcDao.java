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
    private final SimpleJdbcInsert jdbcReviewVoteInsert;

    private static final String TABLE_REVIEWS = "reviews";
    private static final String TABLE_SUB = "subjects";
    private static final String TABLE_REVIEW_VOTE = "reviewVote";

    private static final String TABLE_USERS = "users";

    @Autowired
    public ReviewJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(TABLE_REVIEWS)
                .usingGeneratedKeyColumns("id");
        this.jdbcReviewVoteInsert = new SimpleJdbcInsert(ds)
                .withTableName(TABLE_REVIEW_VOTE);
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
        create(review.getEasy(), review.getTimeDemanding(), review.getText(), review.getSubjectId(),review.getUserId());
    }



    @Override
    public Review create(Integer easy, Integer timeDemanding, String text,String subjectId,long userId ) {
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


    // - - - - - - Review upvotes and downvotes - - - - - -

    @Override
    public boolean userVotedOnReview(Long idUser, Long idReview){
        return jdbcTemplate.query("SELECT * FROM "+TABLE_REVIEW_VOTE+ " WHERE idUser = ? AND idReview = ?",
                ReviewJdbcDao::rowMapperReviewVote,idUser,idReview).stream().findFirst().isPresent();
    }
    @Override
    public void voteReview(Long idUser, Long idReview, int vote){
        Map<String, Object> data = new HashMap<>();

        data.put("idUser", idUser);
        data.put("idReview", idReview);
        data.put("vote", vote);

        jdbcReviewVoteInsert.execute(data);
    }
    @Override
    public void updateVoteOnReview(Long idUser, Long idReview, int vote){
        jdbcTemplate.update("UPDATE " + TABLE_REVIEW_VOTE + " SET vote = ? WHERE idreview = ? AND iduser = ?",
                vote,idReview,idUser);
    }
    private static Integer rowMapperReviewVote(ResultSet rs, int rowNum) throws SQLException{
        return rs.getInt("vote");
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - -


   // - - - - - - Review with subject name - - - - - -
//    @Override
//    public List<Review> getAllUserReviewsWithSubjectName(Long userId) {
//        return jdbcTemplate.query("SELECT * FROM " + TABLE_SUB +" FULL JOIN " + TABLE_REVIEWS + " ON "
//            + TABLE_SUB +".id = " + TABLE_REVIEWS + ".idsub WHERE iduser = ?", ReviewJdbcDao::subjectNameRowMapper, userId);
//
//    }
   @Override
   public List<Review> getAllUserReviewsWithSubjectName(Long userId) {
       return jdbcTemplate.query(completeReviewSqlSubjectName("WHERE r.iduser = ?"), ReviewJdbcDao::subjectNameRowMapper, userId);

   }

//    @Override
//    public List<Review> getAllSubjectReviewsWithUsername(String subjectId, Long userId) {
//        return jdbcTemplate.query("SELECT * FROM " + TABLE_SUB +" FULL JOIN " + TABLE_REVIEWS + " ON "
//            + TABLE_SUB +".id = " + TABLE_REVIEWS + ".idsub WHERE iduser = ?", ReviewJdbcDao::subjectNameRowMapper, userId);
//
//    }

    @Override
    public List<Review> getAllSubjectReviewsWithUsername(String subjectId ) {
        return jdbcTemplate.query(completeReviewSqlUserName("WHERE r.idSub = ? "), ReviewJdbcDao::UsernameRowMapper, subjectId);

    }

//    @Override
//    public List<Review> getAllSubjectReviewsWithUsername(String subjectId ) {
//        return jdbcTemplate.query(completeReviewSqlUserName("WHERE r.idSub = ? "), ReviewJdbcDao::UsernameRowMapper, subjectId);
//
//    }

    private static Review subjectNameRowMapper(ResultSet rs, int rowNum) throws SQLException {
        return new Review(
                rs.getLong("id"),
                rs.getLong("idUser"),
                rs.getString("idSub"),
                rs.getInt("easy"),
                rs.getInt("timeDemanding"),
                rs.getString("revText"),
                rs.getString("subname"),
                rs.getInt("upvotes"),
                rs.getInt("downvotes")
        );
    }
    private static Review UsernameRowMapper(ResultSet rs, int rowNum) throws SQLException {
        return new Review(
                rs.getLong("id"),
                rs.getLong("idUser"),
                rs.getString("username"),
                rs.getString("idSub"),
                rs.getInt("easy"),
                rs.getInt("timeDemanding"),
                rs.getString("revText"),
                rs.getInt("upvotes"),
                rs.getInt("downvotes")
        );
    }
    // - - - - - - - - - - - - - - - - - - - - - - - -

    // - - - - - - Review with subject name and upvotes, downvotes - - - - - -
//    @Override
//    public List<Review> getCompleteReviewsByUserId(Long idUser) {
//        return jdbcTemplate.query(completeReviewSql("WHERE r.idUser = ? "), ReviewJdbcDao::completeReviewRowMapper, idUser);
//    }
//
//    @Override
//    public List<Review> getCompleteReviewsBySubjectId(String idSub) {
//        return jdbcTemplate.query(completeReviewSql("WHERE r.idSub = ? "), ReviewJdbcDao::completeReviewRowMapper, idSub);
//    }


    private String completeReviewSqlSubjectName(String where){
        return
                "SELECT r.id, r.idUser, r.idSub, r.score, r.easy, r.timeDemanding, r.revText, s.subname, " +
                        "sum(CASE WHEN rv.vote = 1 THEN 1 ELSE 0 END) AS upvotes, sum(CASE WHEN rv.vote = -1 THEN 1 ELSE 0 END) AS downvotes " +
                        "FROM " +  TABLE_REVIEWS + " AS r FULL JOIN " + TABLE_SUB +" AS s ON r.idSub = s.id FULL JOIN " +  TABLE_REVIEW_VOTE + " AS rv ON r.id = rv.idReview " +
                        where +
                        " GROUP BY r.id, s.subname";
    }

    private String completeReviewSqlUserName(String where){
        return
                "SELECT r.id, r.idUser, u.username, r.idSub, r.score, r.easy, r.timeDemanding, r.revText, " +
                        "sum(CASE WHEN rv.vote = 1 THEN 1 ELSE 0 END) AS upvotes, sum(CASE WHEN rv.vote = -1 THEN 1 ELSE 0 END) AS downvotes " +
                        "FROM " +  TABLE_REVIEWS + " AS r FULL JOIN " + TABLE_USERS +" AS u ON r.idUser = u.id FULL JOIN " +  TABLE_REVIEW_VOTE + " AS rv ON r.id = rv.idReview " +
                        where +
                        " GROUP BY r.id, r.idUser, u.username";
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - -
}
