package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewStatistic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final RowMapper<Review> ROW_MAPPER = ReviewJdbcDao::rowMapperReview;

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertReview;
    private final SimpleJdbcInsert jdbcReviewVoteInsert;
    private final JdbcTemplate jdbcTemplateReviewStatistic;
    private final SimpleJdbcInsert jdbcInsertReviewStatistic;

    private static final String TABLE_REVIEWS = "reviews";
    private static final String TABLE_SUB = "subjects";
    private static final String TABLE_REVIEW_VOTE = "reviewVote";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_REVIEW_STAT = "subjectReviewStatistics";
    private static final String PAGE_SIZE = "10";

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewJdbcDao.class);
    @Autowired
    public ReviewJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcTemplateReviewStatistic = new JdbcTemplate(ds);
        this.jdbcInsertReviewStatistic = new SimpleJdbcInsert(ds).withTableName(TABLE_REVIEW_STAT);
        this.jdbcReviewVoteInsert = new SimpleJdbcInsert(ds)
                .withTableName(TABLE_REVIEW_VOTE);
        this.jdbcInsertReview = new SimpleJdbcInsert(ds).withTableName(TABLE_REVIEWS).usingGeneratedKeyColumns("id");
    }


    @Override
    public Optional<Review> findById(Long id) {
        return jdbcTemplate.query("SELECT * FROM " + TABLE_REVIEWS + " WHERE id = ?",ROW_MAPPER, id)
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
        create(review.getAnonymous(),review.getEasy(), review.getTimeDemanding(), review.getText(), review.getSubjectId(),review.getUserId());
    }

    @Override
    public Review create(Boolean anonymous,Integer easy, Integer timeDemanding, String text,String subjectId,long userId ) {
        Map<String, Object> data = new HashMap<>();
        data.put("easy", easy);
        data.put("timeDemanding", timeDemanding);
        data.put("revText", text);
        data.put("idSub", subjectId);
        data.put("idUser", userId);
        data.put("useranonymous",anonymous);

        Number key = jdbcInsertReview.executeAndReturnKey(data);
        Review review = new Review(key.longValue(), userId,  subjectId, easy, timeDemanding, text, anonymous);

        LOGGER.info("Review created with id {} by user {} with anonymous visibility {}", review.getId(), review.getUserId(), review.getAnonymous());

        updateStatistics(review);
        return review;

    }

    @Override
    public void delete(Long id) {
        LOGGER.info("Review deleted with id {}", id);

        jdbcTemplate.update("DELETE FROM " + TABLE_REVIEWS + " WHERE id = ?", id);
    }

    @Override
    public void deleteReviewStatistics(Review review){
        Optional<ReviewStatistic> stat = getReviewStatBySubject(review.getSubjectId());
        int easy = 0, medium = 0, hard = 0, timeDemanding =0 ,averageTimeDemanding = 0, notTimeDemanding = 0;
        switch (review.getEasy()){
            case 0: easy--;break;
            case 1: medium--;break;
            case 2: hard--;break;
        }
        switch (review.getTimeDemanding()){
            case 0: notTimeDemanding--;break;
            case 1: averageTimeDemanding--;break;
            case 2: timeDemanding--;break;
        }

        if(stat.isPresent()){
            LOGGER.info("ReviewStatistics deleted from review with id {} by user {} with anonymous visibility {}", review.getId(), review.getUserId(), review.getAnonymous());

            ReviewStatistic reviewStat= stat.get();
            jdbcTemplateReviewStatistic.update("UPDATE " + TABLE_REVIEW_STAT +
                            " SET reviewCount = ?, easyCount = ?, mediumCount = ?, hardCount = ?, " +
                            "notTimeDemandingCount = ?, averageTimeDemandingCount = ?,timeDemandingCount = ? WHERE idSub = ?",
                    reviewStat.getReviewCount() - 1,
                    reviewStat.getEasyCount() + easy,
                    reviewStat.getMediumCount() + medium,
                    reviewStat.getHardCount() + hard,
                    reviewStat.getNotTimeDemandingCount() + notTimeDemanding,
                    reviewStat.getAverageTimeDemandingCount() + averageTimeDemanding,
                    reviewStat.getTimeDemandingCount() + timeDemanding,
                    reviewStat.getIdSub()
            );

        }
    }

    @Override
    public void update(Review review) {
        LOGGER.info("Review updated with id {} by user {}", review.getId(), review.getUserId());

        jdbcTemplate.update("UPDATE " + TABLE_REVIEWS + " SET revtext = ?, " +
                "easy = ?, timedemanding = ?, useranonymous = ? WHERE id = ?",
                review.getText(), review.getEasy(), review.getTimeDemanding(), review.getAnonymous(), review.getId());
    }

    @Override
    public void updateReviewStatistics( Integer easyBefore, Integer timeDemandingBefore, Review review){
        Optional<ReviewStatistic> stat = getReviewStatBySubject(review.getSubjectId());
        int easy = 0, medium = 0, hard = 0, timeDemanding =0, averageTimeDemanding = 0, notTimeDemanding = 0;
        switch (easyBefore){
            case 0: easy--;break;
            case 1: medium--;break;
            case 2: hard--;break;
        }
        switch (review.getEasy()){
            case 0: easy++;break;
            case 1: medium++;break;
            case 2: hard++;break;
        }
        switch (timeDemandingBefore){
            case 0: notTimeDemanding--;break;
            case 1: averageTimeDemanding--;break;
            case 2: timeDemanding--;break;
        }
        switch (review.getTimeDemanding()){
            case 0: notTimeDemanding++;break;
            case 1: averageTimeDemanding++;break;
            case 2: timeDemanding++;break;
        }

        if(stat.isPresent()){
            LOGGER.info("ReviewStatistics updated with id {} by user {} with anonymous visibility {}", review.getId(), review.getUserId(), review.getAnonymous());

            ReviewStatistic reviewStat= stat.get();
            jdbcTemplateReviewStatistic.update("UPDATE " + TABLE_REVIEW_STAT +
                            " SET easyCount = ?, mediumCount = ?, hardCount = ?, " +
                            "notTimeDemandingCount = ?, averageTimeDemandingCount = ? ,timeDemandingCount = ? WHERE idSub = ?",

                    reviewStat.getEasyCount() + easy,
                    reviewStat.getMediumCount() + medium,
                    reviewStat.getHardCount() + hard,
                    reviewStat.getNotTimeDemandingCount() + notTimeDemanding,
                    reviewStat.getAverageTimeDemandingCount() + averageTimeDemanding,
                    reviewStat.getTimeDemandingCount() + timeDemanding,
                    reviewStat.getIdSub()
            );

        }

    }

    // - - - - - REVIEW STATISTICS - - - - -

    private void updateStatistics(Review review){
        Optional<ReviewStatistic> stat = getReviewStatBySubject(review.getSubjectId());
        int easy = 0, medium = 0, hard = 0, timeDemanding =0, averageTimeDemanding = 0, notTimeDemanding = 0;
        switch (review.getEasy()){
            case 0: easy++;break;
            case 1: medium++;break;
            case 2: hard++;break;
        }
        switch (review.getTimeDemanding()){
            case 0: notTimeDemanding++;break;
            case 1: averageTimeDemanding++;break;
            case 2: timeDemanding++;break;
        }

        if(stat.isPresent()){
            ReviewStatistic reviewStat= stat.get();
            jdbcTemplateReviewStatistic.update("UPDATE " + TABLE_REVIEW_STAT +
                    " SET reviewCount = ?, easyCount = ?, mediumCount = ?, hardCount = ?, " +
                    "notTimeDemandingCount = ?, averagetimedemandingcount = ?, timeDemandingCount = ? WHERE idSub = ?",
                    reviewStat.getReviewCount() +  1,
                    reviewStat.getEasyCount() + easy,
                    reviewStat.getMediumCount() + medium,
                    reviewStat.getHardCount() + hard,
                    reviewStat.getNotTimeDemandingCount() + notTimeDemanding,
                    reviewStat.getAverageTimeDemandingCount() + averageTimeDemanding,
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
            data.put("averageTimeDemandingCount",averageTimeDemanding);
            data.put("timeDemandingCount", timeDemanding);
            jdbcInsertReviewStatistic.execute(data);
        }
        LOGGER.info("Updated statistics for review {}", review.getId());
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
            int averageTimeDemandingCount = rs.getInt("averageTimeDemandingCount");
            int timeDemandingCount = rs.getInt("timeDemandingCount");

            final ReviewStatistic subClass = reviewStats.getOrDefault(idSub,
                    new ReviewStatistic(idSub,reviewCount,easyCount,mediumCount,hardCount,notTimeDemandingCount,averageTimeDemandingCount,timeDemandingCount));

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
                rs.getInt("averageTimeDemandingCount"),
                rs.getInt("timeDemandingCount")
        );
    }


    private static Review rowMapperReview(ResultSet rs, int rowNum) throws SQLException {
        return new Review(
                rs.getLong("id"),
                rs.getLong("idUser"),
                rs.getString("idSub"),
                rs.getInt("easy"),
                rs.getInt("timeDemanding"),
                rs.getString("revText"),
                rs.getBoolean("useranonymous")
        );
    }

    @Override
    public Boolean didUserReviewDB(String subjectId, Long userId){
        Optional<Review> review = jdbcTemplate.query("SELECT * FROM " + TABLE_REVIEWS + " WHERE idsub = ? AND iduser = ?", ReviewJdbcDao::rowMapperReview, subjectId, userId).stream().findFirst();

        return review.isPresent();
    }

    private static Integer difficultyRowMapper(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("easy");
    }
    private static Integer timeRowMapper(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("timedemanding");
    }


    // - - - - - - Review upvotes and downvotes - - - - - -

    @Override
    public Integer deleteReviewVoteByReviewId(Long idReview){
        int success = jdbcTemplate.update("DELETE FROM " + TABLE_REVIEW_VOTE + " WHERE idReview = ?", idReview);
        if(success != 0) {
            LOGGER.info("Deleted votes in review {}", idReview);
        } else {
            LOGGER.warn("Votes deletion in review {} failed", idReview);
        }
        return success;
    }

    @Override
    public Integer deleteReviewVote(Long idUser, Long idReview){
        int success = jdbcTemplate.update("DELETE FROM " + TABLE_REVIEW_VOTE + " WHERE idUser = ? AND idReview = ?", idUser,idReview);
        if(success != 0) {
            LOGGER.info("Deleted vote in review {} by user {}", idReview, idUser);
        } else {
            LOGGER.warn("Vote deletion in review {} failed by user {}", idReview, idUser);
        }
        return success;
    }

    @Override
    public boolean userVotedOnReview(Long idUser, Long idReview){
        return jdbcTemplate.query("SELECT * FROM "+TABLE_REVIEW_VOTE+ " WHERE idUser = ? AND idReview = ?",
                ReviewJdbcDao::rowMapperReviewVote,idUser,idReview).stream().findFirst().isPresent();
    }
    private static Integer rowMapperReviewVote(ResultSet rs, int rowNum) throws SQLException{
        return rs.getInt("vote");
    }

    @Override
    public Integer voteReview(Long idUser, Long idReview, int vote){
        Map<String, Object> data = new HashMap<>();

        data.put("idUser", idUser);
        data.put("idReview", idReview);
        data.put("vote", vote);

        LOGGER.info("Review {} voted with value {} by user {} ", idReview, vote,idUser);
        return jdbcReviewVoteInsert.execute(data);
    }

    @Override
    public Integer updateVoteOnReview(Long idUser, Long idReview, int vote){
        LOGGER.info("Updated vote on review {} with value {} for user {}", idReview, vote, idUser);
        return jdbcTemplate.update("UPDATE " + TABLE_REVIEW_VOTE + " SET vote = ? WHERE idreview = ? AND iduser = ?",
                vote,idReview,idUser);
    }

    // key: idReview - value: vote
    @Override
    public Map<Long,Integer> userReviewVoteByIdSubAndIdUser(String idSub, Long idUser){
        return jdbcTemplate.query("SELECT rv.idReview, rv.vote FROM "+ TABLE_REVIEW_VOTE + " AS rv FULL JOIN "+ TABLE_REVIEWS +
                " AS r ON rv.idReview = r.id WHERE r.idSub = ? AND rv.idUser = ?", ReviewJdbcDao::userReviewVoteExtractor,idSub,idUser);
    }
    // key: idReview - value: vote
    @Override
    public Map<Long,Integer> userReviewVoteByIdUser(Long idUser){
        return jdbcTemplate.query("SELECT idReview, vote FROM "+ TABLE_REVIEW_VOTE + " WHERE  idUser = ?", ReviewJdbcDao::userReviewVoteExtractor,idUser);
    }
    private static Map<Long, Integer> userReviewVoteExtractor(ResultSet rs) throws SQLException{
        Map<Long,Integer> res = new HashMap<>();
        while(rs.next()){
            Long id = rs.getLong("idReview");
            Integer vote = rs.getInt("vote");

            res.put(id,vote);
        }

        return res;
    }


    // - - - - - - - - - - - - - - - - - - - - - - - - - -


   // - - - - - - Review with subject name - - - - - -

   @Override
   public List<Review> getAllUserReviewsWithSubjectName(Long userId) {
       return jdbcTemplate.query(completeReviewSqlSubjectName("WHERE r.iduser = ?"), ReviewJdbcDao::subjectNameRowMapper, userId);

   }

    @Override
    public int getAllCountSubjectReviews(String subjectId) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + TABLE_REVIEWS + " WHERE idsub = " + subjectId + "::text",Integer.class);
    }

    @Override
    public List<Review> getAllSubjectReviewsWithUsername(String subjectId, Map<String,String> params) {

        String orderBY =" ORDER BY " + params.getOrDefault("order", "easy") + " " +
                params.getOrDefault("dir", "ASC");
        int offset = Integer.parseInt(params.getOrDefault("pageNum","0")) * Integer.parseInt(PAGE_SIZE);
        String pageNum = " LIMIT " + PAGE_SIZE + " OFFSET " + offset;
        return jdbcTemplate.query(completeReviewSqlUserName("WHERE r.idSub = ? ", orderBY,pageNum), ReviewJdbcDao::UsernameRowMapper, subjectId);
    }


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
                rs.getInt("downvotes"),
                rs.getBoolean("useranonymous")
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
                rs.getInt("downvotes"),
                rs.getBoolean("useranonymous")
        );
    }
    // - - - - - - - - - - - - - - - - - - - - - - - -

    // - - - - - - Review with subject name and upvotes, downvotes - - - - - -
    private String completeReviewSqlSubjectName(String where){
        return
                "SELECT r.id, r.idUser, r.idSub, r.score, r.easy, r.timeDemanding, r.revText, r.useranonymous, s.subname, " +
                        "sum(CASE WHEN rv.vote = 1 THEN 1 ELSE 0 END) AS upvotes, sum(CASE WHEN rv.vote = -1 THEN 1 ELSE 0 END) AS downvotes " +
                        "FROM " +  TABLE_REVIEWS + " AS r FULL JOIN " + TABLE_SUB +" AS s ON r.idSub = s.id FULL JOIN " +  TABLE_REVIEW_VOTE + " AS rv ON r.id = rv.idReview " +
                        where +
                        " GROUP BY r.id, s.subname";
    }

    private String completeReviewSqlUserName(String where,String orderBy,String page){
        return
                "SELECT r.id, r.idUser, u.username, r.idSub, r.score, r.easy, r.timeDemanding, r.revText, r.useranonymous, " +
                        "sum(CASE WHEN rv.vote = 1 THEN 1 ELSE 0 END) AS upvotes, sum(CASE WHEN rv.vote = -1 THEN 1 ELSE 0 END) AS downvotes " +
                        "FROM " +  TABLE_REVIEWS + " AS r FULL JOIN " + TABLE_USERS +" AS u ON r.idUser = u.id FULL JOIN " +  TABLE_REVIEW_VOTE + " AS rv ON r.id = rv.idReview " +
                        where +
                        " GROUP BY r.id, r.idUser, u.username" +
                        orderBy +
                        page;
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - -
}
