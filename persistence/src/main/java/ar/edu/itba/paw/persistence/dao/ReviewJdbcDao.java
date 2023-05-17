package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewStats;
import ar.edu.itba.paw.persistence.constants.Tables;
import ar.edu.itba.paw.persistence.constants.Views;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class ReviewJdbcDao implements ReviewDao {
    private static final String PAGE_SIZE = "10";
    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewJdbcDao.class);


    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertReview;
    private final SimpleJdbcInsert jdbcReviewVoteInsert;
    private final JdbcTemplate jdbcTemplateReviewStatistic;

    @Autowired
    public ReviewJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcTemplateReviewStatistic = new JdbcTemplate(ds);
        this.jdbcReviewVoteInsert = new SimpleJdbcInsert(ds)
            .withTableName(Tables.REVIEW_VOTES);
        this.jdbcInsertReview = new SimpleJdbcInsert(ds).withTableName(Tables.REVIEWS).usingGeneratedKeyColumns("id");
    }


    @Override
    public Optional<Review> findById(final Long id) {
        return jdbcTemplate.query("SELECT * FROM " + Tables.REVIEWS + " WHERE id = ?", ReviewJdbcDao::rowMapperReview, id)
            .stream().findFirst();
    }

    @Override
    public List<Review> getAllBySubject(final String idsub) {
        return jdbcTemplate.query("SELECT * FROM " + Tables.REVIEWS + " WHERE idsub = ?", ReviewJdbcDao::rowMapperReview, idsub);
    }

    @Override
    public List<Review> getAll() {
        return jdbcTemplate.query("SELECT * FROM " + Tables.REVIEWS, ReviewJdbcDao::rowMapperReview);
    }

    @Override
    public void insert(final Review review) {
        create(review);
    }

    @Override
    public Review create(final Review review) {
        Map<String, Object> data = new HashMap<>();
        data.put("easy", review.getEasy());
        data.put("timeDemanding", review.getTimeDemanding());
        data.put("revText", review.getText());
        data.put("idSub", review.getSubjectId());
        data.put("idUser", review.getUserId());
        data.put("useranonymous", review.getAnonymous());

        Number key = jdbcInsertReview.executeAndReturnKey(data);
        Review newRev = Review.builderFrom(review)
            .id(key.longValue())
            .build();

        LOGGER.info("Review created with id {} by user {} with anonymous visibility {}", review.getId(), review.getUserId(), review.getAnonymous());
        return newRev;
    }

    @Override
    public void delete(final Long id) {
        LOGGER.info("Review deleted with id {}", id);

        jdbcTemplate.update("DELETE FROM " + Tables.REVIEWS + " WHERE id = ?", id);
    }

    @Override
    public void update(final Review review) {
        LOGGER.info("Review updated with id {} by user {}", review.getId(), review.getUserId());

        jdbcTemplate.update("UPDATE " + Tables.REVIEWS + " SET revtext = ?, " +
                "easy = ?, timedemanding = ?, useranonymous = ? WHERE id = ?",
            review.getText(), review.getEasy(), review.getTimeDemanding(), review.getAnonymous(), review.getId());
    }

    // - - - - - REVIEW STATISTICS - - - - -

    @Override
    public Optional<ReviewStats> getReviewStatBySubject(final String idSub) {
        return jdbcTemplate.query("SELECT * FROM " + Views.REVIEW_STATS + " WHERE idSub = ?", ReviewJdbcDao::rowMapperReviewStatistic, idSub)
            .stream().findFirst();
    }

    private String generateSubjectListQuery(final int size) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(Views.REVIEW_STATS).append(" WHERE idSub IN (");

        // TODO change because its unsafe?
        for (int i = 0; i < size; i++) {
            sql.append(" ? ");
            if (i + 1 < size) {
                sql.append(", ");
            }
        }
        sql.append(")");
        return sql.toString();
    }

    @Override
    public List<ReviewStats> getReviewStatBySubjectList(final List<String> idSubs) {
        if (idSubs.isEmpty()) {
            return new ArrayList<>();
        }
        String sql = generateSubjectListQuery(idSubs.size());
        return jdbcTemplate.query(sql, ReviewJdbcDao::rowMapperReviewStatistic, idSubs.toArray());
    }

    @Override
    public Map<String, ReviewStats> getReviewStatMapBySubjectList(final List<String> idSubs) {
        if (idSubs.isEmpty()) {
            return new HashMap<>();
        }
        String sql = generateSubjectListQuery(idSubs.size());

        return jdbcTemplate.query(sql, ReviewJdbcDao::reviewStatisticMapExtractor, idSubs.toArray());
    }

    private static Map<String, ReviewStats> reviewStatisticMapExtractor(final ResultSet rs) throws SQLException {
        final Map<String, ReviewStats> reviewStats = new HashMap<>();
        while (rs.next()) {
            String idSub = rs.getString("idSub");
            int reviewCount = rs.getInt("reviewCount");
            int easyCount = rs.getInt("easyCount");
            int mediumCount = rs.getInt("mediumCount");
            int hardCount = rs.getInt("hardCount");
            int notTimeDemandingCount = rs.getInt("notTimeDemandingCount");
            int averageTimeDemandingCount = rs.getInt("averageTimeDemandingCount");
            int timeDemandingCount = rs.getInt("timeDemandingCount");

            final ReviewStats subClass = reviewStats.getOrDefault(idSub,
                new ReviewStats(idSub, reviewCount, easyCount, mediumCount, hardCount, notTimeDemandingCount, averageTimeDemandingCount, timeDemandingCount));

            reviewStats.put(idSub, subClass);
        }
        return reviewStats;
    }

    private static ReviewStats rowMapperReviewStatistic(final ResultSet rs, final int rowNum) throws SQLException {
        return new ReviewStats(
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


    private static Review rowMapperReview(final ResultSet rs, final int rowNum) throws SQLException {
        return Review.builder()
            .id(rs.getLong("id"))
            .userId(rs.getLong("idUser"))
            .subjectId(rs.getString("idSub"))
            .easy(rs.getInt("easy"))
            .timeDemanding(rs.getInt("timeDemanding"))
            .text(rs.getString("revText"))
            .anonymous(rs.getBoolean("useranonymous"))
            .build();
    }

    @Override
    public Boolean didUserReviewDB(final String subjectId, final Long userId) {
        Optional<Review> review = jdbcTemplate.query("SELECT * FROM " + Tables.REVIEWS + " WHERE idsub = ? AND iduser = ?", ReviewJdbcDao::rowMapperReview, subjectId, userId).stream().findFirst();

        return review.isPresent();
    }

    private static Integer difficultyRowMapper(final ResultSet rs, final int rowNum) throws SQLException {
        return rs.getInt("easy");
    }

    private static Integer timeRowMapper(final ResultSet rs, final int rowNum) throws SQLException {
        return rs.getInt("timedemanding");
    }


    // - - - - - - Review upvotes and downvotes - - - - - -

    @Override
    public Integer deleteReviewVoteByReviewId(final Long idReview) {
        int success = jdbcTemplate.update("DELETE FROM " + Tables.REVIEW_VOTES + " WHERE idReview = ?", idReview);
        if (success != 0) {
            LOGGER.info("Deleted votes in review {}", idReview);
        } else {
            LOGGER.warn("Votes deletion in review {} failed", idReview);
        }
        return success;
    }

    @Override
    public Integer deleteReviewVote(final Long idUser, final Long idReview) {
        int success = jdbcTemplate.update("DELETE FROM " + Tables.REVIEW_VOTES + " WHERE idUser = ? AND idReview = ?", idUser, idReview);
        if (success != 0) {
            LOGGER.info("Deleted vote in review {} by user {}", idReview, idUser);
        } else {
            LOGGER.warn("Vote deletion in review {} failed by user {}", idReview, idUser);
        }
        return success;
    }

    @Override
    public boolean userVotedOnReview(final Long idUser, final Long idReview) {
        return jdbcTemplate.query("SELECT * FROM " + Tables.REVIEW_VOTES + " WHERE idUser = ? AND idReview = ?",
            ReviewJdbcDao::rowMapperReviewVote, idUser, idReview).stream().findFirst().isPresent();
    }

    private static Integer rowMapperReviewVote(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("vote");
    }

    @Override
    public Integer voteReview(final Long idUser, final Long idReview, final int vote) {
        Map<String, Object> data = new HashMap<>();

        data.put("idUser", idUser);
        data.put("idReview", idReview);
        data.put("vote", vote);

        LOGGER.info("Review {} voted with value {} by user {} ", idReview, vote, idUser);
        return jdbcReviewVoteInsert.execute(data);
    }

    @Override
    public Integer updateVoteOnReview(final Long idUser, final Long idReview, final int vote) {
        LOGGER.info("Updated vote on review {} with value {} for user {}", idReview, vote, idUser);
        return jdbcTemplate.update("UPDATE " + Tables.REVIEW_VOTES + " SET vote = ? WHERE idreview = ? AND iduser = ?",
            vote, idReview, idUser);
    }

    // key: idReview - value: vote
    @Override
    public Map<Long, Integer> userReviewVoteByIdSubAndIdUser(final String idSub, final Long idUser) {
        return jdbcTemplate.query("SELECT rv.idReview, rv.vote FROM " + Tables.REVIEW_VOTES + " AS rv FULL JOIN " + Tables.REVIEWS +
            " AS r ON rv.idReview = r.id WHERE r.idSub = ? AND rv.idUser = ?", ReviewJdbcDao::userReviewVoteExtractor, idSub, idUser);
    }

    // key: idReview - value: vote
    @Override
    public Map<Long, Integer> userReviewVoteByIdUser(final Long idUser) {
        return jdbcTemplate.query("SELECT idReview, vote FROM " + Tables.REVIEW_VOTES + " WHERE  idUser = ?", ReviewJdbcDao::userReviewVoteExtractor, idUser);
    }

    private static Map<Long, Integer> userReviewVoteExtractor(final ResultSet rs) throws SQLException {
        Map<Long, Integer> res = new HashMap<>();
        while (rs.next()) {
            Long id = rs.getLong("idReview");
            Integer vote = rs.getInt("vote");

            res.put(id, vote);
        }

        return res;
    }


    // - - - - - - - - - - - - - - - - - - - - - - - - - -


    // - - - - - - Review with subject name - - - - - -

    @Override
    public List<Review> getAllUserReviewsWithSubjectName(final Long userId,final Map<String, String> params) {
        int offset = Integer.parseInt(params.getOrDefault("pageNum", "0")) * Integer.parseInt(PAGE_SIZE);
        String pageNum = " LIMIT " + PAGE_SIZE + " OFFSET " + offset;
        return jdbcTemplate.query(completeReviewSqlSubjectName("WHERE r.iduser = ?",pageNum), ReviewJdbcDao::subjectNameRowMapper, userId);
    }

    @Override
    public int getTotalPagesFromUserReviews(final Long userId){
        List<Review> reviews = jdbcTemplate.query(completeReviewSqlSubjectName("WHERE r.iduser = ?"), ReviewJdbcDao::subjectNameRowMapper, userId);
        int pageSize = Integer.parseInt(PAGE_SIZE);
        return reviews.size()%pageSize == 0? (reviews.size() / pageSize)-1 : (reviews.size()/pageSize);
    }

    @Override
    public int getTotalPagesForReviews(final String subjectId) {
        int amountReviews = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + Tables.REVIEWS + " WHERE idsub = " + subjectId + "::text", Integer.class);
        int pageSize = Integer.parseInt(PAGE_SIZE);
        return amountReviews%pageSize == 0? (amountReviews / pageSize)-1 : (amountReviews/pageSize);
    }

    @Override
    public List<Review> getAllSubjectReviewsWithUsername(final String subjectId, final Map<String, String> params) {

        String orderBY = " ORDER BY " + params.getOrDefault("order", "easy") + " " +
            params.getOrDefault("dir", "ASC");
        int offset = Integer.parseInt(params.getOrDefault("pageNum", "0")) * Integer.parseInt(PAGE_SIZE);
        String pageNum = " LIMIT " + PAGE_SIZE + " OFFSET " + offset;
        return jdbcTemplate.query(completeReviewSqlUserName("WHERE r.idSub = ? ", orderBY, pageNum), ReviewJdbcDao::UsernameRowMapper, subjectId);
    }


    private static Review subjectNameRowMapper(final ResultSet rs, final int rowNum) throws SQLException {
        return Review.builder()
            .id(rs.getLong("id"))
            .userId(rs.getLong("idUser"))
            .subjectId(rs.getString("idSub"))
            .easy(rs.getInt("easy"))
            .timeDemanding(rs.getInt("timeDemanding"))
            .text(rs.getString("revText"))
            .subjectName(rs.getString("subname"))
            .upvotes(rs.getInt("upvotes"))
            .downvotes(rs.getInt("downvotes"))
            .anonymous(rs.getBoolean("useranonymous"))
            .build();
    }

    private static Review UsernameRowMapper(final ResultSet rs, final int rowNum) throws SQLException {
        return Review.builder()
            .id(rs.getLong("id"))
            .userId(rs.getLong("idUser"))
            .username(rs.getString("username"))
            .subjectId(rs.getString("idSub"))
            .easy(rs.getInt("easy"))
            .timeDemanding(rs.getInt("timeDemanding"))
            .text(rs.getString("revText"))
            .upvotes(rs.getInt("upvotes"))
            .downvotes(rs.getInt("downvotes"))
            .anonymous(rs.getBoolean("useranonymous"))
            .build();
    }
    // - - - - - - - - - - - - - - - - - - - - - - - -

    // - - - - - - Review with subject name and upvotes, downvotes - - - - - -
    private String completeReviewSqlSubjectName(final String where,final String pageOffset) {
        return
            "SELECT r.id, r.idUser, r.idSub, r.score, r.easy, r.timeDemanding, r.revText, r.useranonymous, s.subname, " +
                "sum(CASE WHEN rv.vote = 1 THEN 1 ELSE 0 END) AS upvotes, sum(CASE WHEN rv.vote = -1 THEN 1 ELSE 0 END) AS downvotes " +
                "FROM " + Tables.REVIEWS + " AS r FULL JOIN " + Tables.SUBJECTS + " AS s ON r.idSub = s.id FULL JOIN " + Tables.REVIEW_VOTES + " AS rv ON r.id = rv.idReview " +
                where +
                " GROUP BY r.id, s.subname" + pageOffset;
    }

    private String completeReviewSqlSubjectName(final String where) {
        return
                "SELECT r.id, r.idUser, r.idSub, r.score, r.easy, r.timeDemanding, r.revText, r.useranonymous, s.subname, " +
                        "sum(CASE WHEN rv.vote = 1 THEN 1 ELSE 0 END) AS upvotes, sum(CASE WHEN rv.vote = -1 THEN 1 ELSE 0 END) AS downvotes " +
                        "FROM " + Tables.REVIEWS + " AS r FULL JOIN " + Tables.SUBJECTS + " AS s ON r.idSub = s.id FULL JOIN " + Tables.REVIEW_VOTES + " AS rv ON r.id = rv.idReview " +
                        where +
                        " GROUP BY r.id, s.subname";
    }

    private String completeReviewSqlUserName(final String where, final String orderBy, String page) {
        return
            "SELECT r.id, r.idUser, u.username, r.idSub, r.score, r.easy, r.timeDemanding, r.revText, r.useranonymous, " +
                "sum(CASE WHEN rv.vote = 1 THEN 1 ELSE 0 END) AS upvotes, sum(CASE WHEN rv.vote = -1 THEN 1 ELSE 0 END) AS downvotes " +
                "FROM " + Tables.REVIEWS + " AS r FULL JOIN " + Tables.USERS + " AS u ON r.idUser = u.id FULL JOIN " + Tables.REVIEW_VOTES + " AS rv ON r.id = rv.idReview " +
                where +
                " GROUP BY r.id, r.idUser, u.username" +
                orderBy +
                page;
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - -
}
