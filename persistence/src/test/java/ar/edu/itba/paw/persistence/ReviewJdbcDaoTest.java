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
    private JdbcTemplate jdbcTemplateUsers;
    private JdbcTemplate jdbcTemplateSubjects;

    private JdbcTemplate jdbcTemplateReviewStat;

    private JdbcTemplate jdbcTemplateImage;

    private JdbcTemplate jdbcTemplateVote;

    @Autowired
    private DataSource ds;

    @Autowired
    private ReviewJdbcDao reviewDao;

    @Before
    public void setup(){
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcTemplateUsers = new JdbcTemplate(ds);
        jdbcTemplateSubjects = new JdbcTemplate(ds);
        jdbcTemplateReviewStat= new JdbcTemplate(ds);
        jdbcTemplateImage = new JdbcTemplate(ds);
        jdbcTemplateVote = new JdbcTemplate(ds);

        JdbcTestUtils.deleteFromTables(jdbcTemplateVote, "reviewvote");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "reviews");
        JdbcTestUtils.deleteFromTables(jdbcTemplateUsers, "users");
        JdbcTestUtils.deleteFromTables(jdbcTemplateSubjects, "subjects");
        JdbcTestUtils.deleteFromTables(jdbcTemplateImage, "images");
    }

    @Test
    public void testFindById(){
        jdbcTemplateImage.execute("INSERT INTO images VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplateUsers.execute("INSERT INTO users(id, email, pass, username, image_id) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ")");
        jdbcTemplateSubjects.execute("INSERT INTO subjects(id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
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
        jdbcTemplateImage.execute("INSERT INTO images VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplateUsers.execute("INSERT INTO users(id, email, pass, username, image_id) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ")");
        jdbcTemplateSubjects.execute("INSERT INTO subjects(id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");

        Review review = reviewDao.create(ANONYMOUS, EASY, TIMEDEMANDING, TEXT, SUBJECTID, USERID);

        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "reviews"));
        Assert.assertEquals(USERID, review.getUserId());
        Assert.assertEquals(SUBJECTID, review.getSubjectId());
        Assert.assertEquals(TEXT, review.getText());
    }

    @Test
    public void testDelete(){
        jdbcTemplateImage.execute("INSERT INTO images VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplateUsers.execute("INSERT INTO users(id, email, pass, username, image_id) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ")");
        jdbcTemplateSubjects.execute("INSERT INTO subjects(id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO reviews(id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID + ", " + USERID + ", '" + SUBJECTID + "', " + SCORE + ", " + EASY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")" );

        reviewDao.delete(ID);

        Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "reviews"));
    }

    @Test
    public void testUpdate(){
        jdbcTemplateImage.execute("INSERT INTO images VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplateUsers.execute("INSERT INTO users(id, email, pass, username, image_id) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ")");
        jdbcTemplateSubjects.execute("INSERT INTO subjects(id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
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
        jdbcTemplateSubjects.execute("INSERT INTO subjects(id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplateReviewStat.execute("INSERT INTO subjectreviewstatistics(idsub, reviewcount, easycount, mediumcount, hardcount, nottimedemandingcount, averagetimedemandingcount, timedemandingcount) " +
                "VALUES ('" + SUBJECTID + "', " + REVIEWCOUNT + ", " + EASYCOUNT + ", " + MEDIUMCOUNT + ", " + HARDCOUNT + ", " + NOTTIMEDEMANDINGCOUNT + ", " + AVERAGETIMEDEMANDING + ", " + TIMEDEMANDINGCOUNT + ")" );

        Optional<ReviewStatistic> reviewStatistic = reviewDao.getReviewStatBySubject(SUBJECTID);
        Optional<ReviewStatistic> reviewStatistic2 = reviewDao.getReviewStatBySubject("12.1");

        Assert.assertFalse(reviewStatistic2.isPresent());
        Assert.assertTrue(reviewStatistic.isPresent());
        Assert.assertEquals(EASYCOUNT, reviewStatistic.get().getEasyCount());
    }

    @Test
    public void testVoteReview(){
        jdbcTemplateImage.execute("INSERT INTO images VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplateUsers.execute("INSERT INTO users(id, email, pass, username, image_id) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ")");
        jdbcTemplateSubjects.execute("INSERT INTO subjects(id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO reviews(id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID + ", " + USERID + ", '" + SUBJECTID + "', " + SCORE + ", " + EASY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")" );


        reviewDao.voteReview(USERID, ID, VOTE);

        String query = "iduser = " + USERID + " AND idreview = " + ID + " AND vote = " + VOTE;
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplateVote, "reviewvote", query) );

    }

    @Test
    public void testDeleteReviewVote(){
        jdbcTemplateImage.execute("INSERT INTO images VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplateUsers.execute("INSERT INTO users(id, email, pass, username, image_id) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ")");
        jdbcTemplateSubjects.execute("INSERT INTO subjects(id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO reviews(id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID + ", " + USERID + ", '" + SUBJECTID + "', " + SCORE + ", " + EASY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")" );
        jdbcTemplateVote.execute("INSERT INTO reviewvote VALUES (" + USERID + ", " + ID + ", " + VOTE + ")");


        reviewDao.deleteReviewVote(USERID, ID);

        String query = "iduser = " + USERID + " AND idreview = " + ID + " AND vote = " + VOTE;
        Assert.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplateVote, "reviewvote", query));
    }
}
