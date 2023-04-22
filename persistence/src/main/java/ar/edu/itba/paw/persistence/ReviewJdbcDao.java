package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewStatistic;
import ar.edu.itba.paw.models.SubjectClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.*;

@Repository
public class ReviewJdbcDao implements ReviewDao {
    private static final RowMapper<Review> ROW_MAPPER = ReviewJdbcDao::rowMapperReview;

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertReview;

    private final JdbcTemplate jdbcTemplateReviewStatistic;
    private final SimpleJdbcInsert jdbcInsertReviewStatistic;


    private static final String TABLE_REVIEWS = "reviews";
    private static final String TABLE_REVIEW_STAT = "subjectReviewStatistics";

    @Autowired
    public ReviewJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsertReview = new SimpleJdbcInsert(ds).withTableName(TABLE_REVIEWS).usingGeneratedKeyColumns("id");
        this.jdbcTemplateReviewStatistic = new JdbcTemplate(ds);
        this.jdbcInsertReviewStatistic = new SimpleJdbcInsert(ds).withTableName(TABLE_REVIEW_STAT);
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

        Number key = jdbcInsertReview.executeAndReturnKey(data);

        Review review = new Review(key.longValue(), userId, userEmail, subjectId, easy, timeDemanding, text);

        updateStatistics(review);

        return review;
    }

    @Override
    public void delete(Long integer) {

    }

    @Override
    public void update(Review review) {

    }

    // - - - - - REVIEW STATISTICS - - - - -

    private void updateStatistics(Review review){
        Optional<ReviewStatistic> stat = getReviewStatBySubject(review.getSubjectId());
        int easy = 0, medium = 0, hard = 0, timeDemanding =0 , notTimeDemanding = 0;
        switch (review.getEasy()){
            case 0: easy++;break;
            case 1: medium++;break;
            case 2: hard++;break;
        }
        switch (review.getTimeDemanding()){
            case 0: notTimeDemanding++;break;
            case 1: timeDemanding++;break;
        }

        if(stat.isPresent()){
            ReviewStatistic reviewStat= stat.get();
            jdbcTemplateReviewStatistic.update("UPDATE " + TABLE_REVIEW_STAT +
                    " SET reviewCount = ?, easyCount = ?, mediumCount = ?, hardCount = ?, " +
                    "notTimeDemandingCount = ?, timeDemandingCount = ? WHERE idSub = ?",
                    reviewStat.getReviewCount() +  1,
                    reviewStat.getEasyCount() + easy,
                    reviewStat.getMediumCount() + medium,
                    reviewStat.getHardCount() + hard,
                    reviewStat.getNotTimeDemandingCount() + notTimeDemanding,
                    reviewStat.getTimeDemandingCount() + timeDemanding,
                    reviewStat.getIdSub()
            );

        }
        else{
            Map<String, Object> data = new HashMap<>();
            data.put("idSub", review.getSubjectId());
            data.put("reviewCount", 1);
            data.put("easyCount", easy);
            data.put("mediumCount", medium);
            data.put("hardCount", hard);
            data.put("notTimeDemandingCount", notTimeDemanding);
            data.put("timeDemandingCount", timeDemanding);
            jdbcInsertReviewStatistic.execute(data);
        }
    }


    // This method is slow and costly. It is only meant to be used for table migration
    // and not during normal execution.
    public void recalculateStatistics(){
        jdbcTemplateReviewStatistic.execute("DELETE FROM " + TABLE_REVIEW_STAT);

        List<Review> reviews = getAll();
        for(Review review : reviews){
            updateStatistics(review);
        }
    }

    @Override
    public Optional<ReviewStatistic> getReviewStatBySubject(String idSub){
        return jdbcTemplate.query("SELECT * FROM " + TABLE_REVIEW_STAT + " WHERE idSub = ?", ReviewJdbcDao::rowMapperReviewStatistic, idSub)
                .stream().findFirst();
    }

    private String generateSubjectListQuery(List<String> idSubs){
        StringBuilder sql = new StringBuilder("SELECT * FROM " ).append(TABLE_REVIEW_STAT).append(" WHERE idSub IN (");

        // TODO change because its unsafe?
        Iterator<String> iter = idSubs.iterator();
        while(iter.hasNext()){
            sql.append("'").append(iter.next()).append("'");
            if(iter.hasNext()){
                sql.append(", ");
            }
        }
        sql.append(")");
        return sql.toString();
    }

    @Override
    public List<ReviewStatistic> getReviewStatBySubjectList(List<String> idSubs){
        if(idSubs.isEmpty()){
            return new ArrayList<>();
        }
        String sql = generateSubjectListQuery(idSubs);
        return jdbcTemplate.query(sql, ReviewJdbcDao::rowMapperReviewStatistic);
    }

    @Override
    public Map<String, ReviewStatistic> getReviewStatMapBySubjectList(List<String> idSubs){
        if(idSubs.isEmpty()){
            return new HashMap<>();
        }
        String sql = generateSubjectListQuery(idSubs);

        return jdbcTemplate.query(sql, ReviewJdbcDao::reviewStatisticMapExtractor);
    }

    private static Map<String,ReviewStatistic> reviewStatisticMapExtractor(ResultSet rs) throws SQLException{
        final Map<String, ReviewStatistic> reviewStats = new HashMap<>();
        while (rs.next()) {
            String idSub = rs.getString("idSub");
            int reviewCount = rs.getInt("reviewCount");
            int easyCount = rs.getInt("easyCount");
            int mediumCount = rs.getInt("mediumCount");
            int hardCount = rs.getInt("hardCount");
            int notTimeDemandingCount = rs.getInt("notTimeDemandingCount");
            int timeDemandingCount = rs.getInt("timeDemandingCount");

            final ReviewStatistic subClass = reviewStats.getOrDefault(idSub,
                    new ReviewStatistic(idSub,reviewCount,easyCount,mediumCount,hardCount,notTimeDemandingCount,timeDemandingCount));

            reviewStats.put(idSub, subClass);
        }
        return reviewStats;
    }

    private static ReviewStatistic rowMapperReviewStatistic(ResultSet rs, int rowNum) throws SQLException {
        return new ReviewStatistic(
                rs.getString("idSub"),
                rs.getInt("reviewCount"),
                rs.getInt("easyCount"),
                rs.getInt("mediumCount"),
                rs.getInt("hardCount"),
                rs.getInt("notTimeDemandingCount"),
                rs.getInt("timeDemandingCount")
        );
    }


    private static Review rowMapperReview(ResultSet rs, int rowNum) throws SQLException {
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
}
