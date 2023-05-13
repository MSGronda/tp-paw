package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewStatistic;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ReviewJdbcDaoTest {

    private static final long ID = 1;
    private static final long USERID = 2;
    private static final String SUBJECTID = "72.33";

    private static final int SCORE = 2;
    private static final int EASY = 0;

    private static final int HARD = 2;
    private static final int TIMEDEMANDING = 2;

    private static final int NOTTIMEDEMANDING = 0;


    private static final String TEXT = "esto es una resena";
    private static final Boolean ANONYMOUS = false;

    private static final int NEWEASY = 1;
    private static final int NEWTIMEDEMANDING = 0;
    private static final String NEWTEXT = "otra resena";
    private static final Boolean NEWANONYMOUS = true;

    private static final String EMAIL = "a@gmail.com";

    private static final String PASSWORD = "asdf";

    private static final String USERNAME = "Carlos";

    private static final String NAME = "sistemas";
    private static final String DEPARTMENT = "tractores";
    private static final int CREDITS = 3;

    private static final int REVIEWCOUNT = 10;
    private static final int EASYCOUNT = 5;

    private static final int MEDIUMCOUNT = 3;
    private static final int HARDCOUNT = 2;
    private static final int NOTTIMEDEMANDINGCOUNT = 5;

    private static final int AVERAGETIMEDEMANDING = 1;
    private static final int TIMEDEMANDINGCOUNT = 4;

    private static final long IMAGEID = 1;

    private static final byte[] IMAGE = null;

    private static final int VOTE = 1;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource ds;

    @Autowired
    private ReviewJdbcDao reviewDao;

    @Before
    public void setup(){
        jdbcTemplate = new JdbcTemplate(ds);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, "reviewvote");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "reviews");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "subjects");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "images");
    }

    @Test
    public void testFindById(){
        jdbcTemplate.execute("INSERT INTO images VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username, image_id) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ")");
        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO reviews(id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID + ", " + USERID + ", '" + SUBJECTID + "', " + SCORE + ", " + EASY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")" );

        Optional<Review> review = reviewDao.findById(ID);

        Assert.assertTrue(review.isPresent());
        Assert.assertEquals(USERID, review.get().getUserId());
        Assert.assertEquals(SUBJECTID, review.get().getSubjectId());
        Assert.assertEquals(TEXT, review.get().getText());

    }

    @Test
    public void testCreate(){
        jdbcTemplate.execute("INSERT INTO images VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username, image_id) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ")");
        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");

        Review review = reviewDao.create(ANONYMOUS, EASY, TIMEDEMANDING, TEXT, SUBJECTID, USERID);

        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "reviews"));
        Assert.assertEquals(USERID, review.getUserId());
        Assert.assertEquals(SUBJECTID, review.getSubjectId());
        Assert.assertEquals(TEXT, review.getText());
    }

    @Test
    public void testDelete(){
        jdbcTemplate.execute("INSERT INTO images VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username, image_id) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ")");
        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO reviews(id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID + ", " + USERID + ", '" + SUBJECTID + "', " + SCORE + ", " + EASY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")" );

        reviewDao.delete(ID);

        Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "reviews"));
    }

    @Test
    public void testUpdate(){
        jdbcTemplate.execute("INSERT INTO images VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username, image_id) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ")");
        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO reviews(id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID + ", " + USERID + ", '" + SUBJECTID + "', " + SCORE + ", " + EASY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")" );

        //update is only for the text, easy, timedemanding and anonymous
        Review review = new Review(ID, USERID, SUBJECTID, NEWEASY, NEWTIMEDEMANDING, NEWTEXT, NEWANONYMOUS);
        reviewDao.update(review);

        String oldQuery = "revtext = '" + TEXT + "' AND useranonymous = " + ANONYMOUS + " AND easy = " + EASY + " AND timedemanding = " + TIMEDEMANDING;
        Assert.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviews", oldQuery));
        String query = "revtext = '" + NEWTEXT + "' AND useranonymous = " + NEWANONYMOUS + " AND easy = " + NEWEASY + " AND timedemanding = " + NEWTIMEDEMANDING;
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviews", query));
    }

    @Test
    public void testGetReviewStatBySubject(){
        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO reviews(idsub, iduser, easy, revtext) VALUES" +
                " ('" + SUBJECTID + "', " + USERID + ", " + EASY + ", '" + TEXT + "')");

        Optional<ReviewStatistic> reviewStats = reviewDao.getReviewStatBySubject(SUBJECTID);
        Optional<ReviewStatistic> reviewStats2 = reviewDao.getReviewStatBySubject("12.1");

        Assert.assertFalse(reviewStats2.isPresent());
        Assert.assertTrue(reviewStats.isPresent());
        Assert.assertEquals(1, reviewStats.get().getEasyCount());
        Assert.assertEquals(0, reviewStats.get().getMediumCount());
        Assert.assertEquals(0, reviewStats.get().getHardCount());
        Assert.assertEquals(1, reviewStats.get().getReviewCount());
    }

    @Test
    public void testVoteReview(){
        jdbcTemplate.execute("INSERT INTO images VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO reviews(id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID + ", " + USERID + ", '" + SUBJECTID + "', " + SCORE + ", " + EASY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")" );


        reviewDao.voteReview(USERID, ID, VOTE);

        String query = "iduser = " + USERID + " AND idreview = " + ID + " AND vote = " + VOTE;
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviewvote", query) );

    }

    @Test
    public void testDeleteReviewVote(){
        jdbcTemplate.execute("INSERT INTO images VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplate.execute("INSERT INTO users(id, email, pass, username, image_id) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ")");
        jdbcTemplate.execute("INSERT INTO subjects(id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO reviews(id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID + ", " + USERID + ", '" + SUBJECTID + "', " + SCORE + ", " + EASY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")" );
        jdbcTemplate.execute("INSERT INTO reviewvote VALUES (" + USERID + ", " + ID + ", " + VOTE + ")");


        reviewDao.deleteReviewVote(USERID, ID);

        String query = "iduser = " + USERID + " AND idreview = " + ID + " AND vote = " + VOTE;
        Assert.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviewvote", query));
    }
}
