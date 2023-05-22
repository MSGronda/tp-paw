package ar.edu.itba.paw.persistence.dao.jdbc;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewStats;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.dao.jdbc.ReviewJdbcDao;
import org.junit.After;
import ar.edu.itba.paw.persistence.constants.Tables;
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
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ReviewJdbcDaoTest {

    private static final long ID = 1;
    private static final long ID2 = 2;


    private static final long USERID = 2;

    private static final long USERID2 = 12;

    private static final String SUBJECTID = "72.33";
    private static final String SUBJECTID2 = "86.55";


    private static final int SCORE = 2;
    private static final int DIFFICULTY = 0;

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

    private static final String EMAIL2 = "b@gmail.com";

    private static final String PASSWORD2= "fdsa";

    private static final String USERNAME2 = "Facundo";

    private static final String NAME = "sistemas";
    private static final String DEPARTMENT = "tractores";

    private static final String NAME2 = "otroNombre";
    private static final String DEPARTMENT2 = "escabadora";
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

    private static final int NEWVOTE = -1;
    private static final String PARAM_PAGE_NUM="pageNum";
    private static final String PAGE_ZERO="0";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource ds;

    @Autowired
    private ReviewJdbcDao reviewDao;

    @Before
    public void setup(){
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @After
    public void clearDb(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, Tables.REVIEW_VOTES);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, Tables.REVIEWS);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, Tables.USERS);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, Tables.SUBJECTS);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, Tables.IMG);
    }

    @Test
    public void testFindById(){
        jdbcTemplate.execute("INSERT INTO " + Tables.IMG + " VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username, image_id) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEWS + " (id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID + ", " + USERID + ", '" + SUBJECTID + "', " + SCORE + ", " + DIFFICULTY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")" );

        Optional<Review> review = reviewDao.findById(ID);

        Assert.assertTrue(review.isPresent());
        Assert.assertEquals(USERID, review.get().getUserId());
        Assert.assertEquals(SUBJECTID, review.get().getSubjectId());
        Assert.assertEquals(TEXT, review.get().getText());

    }

    @Test
    public void testGetAllBySubject(){
        jdbcTemplate.execute("INSERT INTO " + Tables.IMG + " VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username, image_id) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username, image_id) VALUES (" + USERID2 + ", '" + EMAIL2 + "', '" + PASSWORD2 + "', '" + USERNAME2 + "', " + IMAGEID + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEWS + " (id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID + ", " + USERID + ", '" + SUBJECTID + "', " + SCORE + ", " + DIFFICULTY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")" );
        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEWS + " (id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID2 + ", " + USERID2 + ", '" + SUBJECTID + "', " + SCORE + ", " + HARD + ", " + NOTTIMEDEMANDING+ ", '" + TEXT + "', " + ANONYMOUS + ")" );

        List<Review> list = reviewDao.getAllBySubject(SUBJECTID);
        List<Review> list2 = reviewDao.getAllBySubject(SUBJECTID2);

        Assert.assertEquals(2, list.size());
        Assert.assertEquals(DIFFICULTY, list.get(0).getEasy());
        Assert.assertEquals(HARD, list.get(1).getEasy());
        Assert.assertEquals(USERID, list.get(0).getUserId());
        Assert.assertEquals(USERID2, list.get(1).getUserId());

        Assert.assertEquals(0, list2.size());

    }

    @Test
    public void testGetAll(){
        jdbcTemplate.execute("INSERT INTO " + Tables.IMG + " VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username, image_id) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES (" + SUBJECTID2 + ", '" + NAME2 + "', '" + DEPARTMENT2 + "', " + CREDITS + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEWS + " (id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID + ", " + USERID + ", '" + SUBJECTID + "', " + SCORE + ", " + DIFFICULTY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")" );
        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEWS + " (id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID2 + ", " + USERID + ", '" + SUBJECTID2 + "', " + SCORE + ", " + HARD + ", " + NOTTIMEDEMANDING+ ", '" + TEXT + "', " + ANONYMOUS + ")" );

        List<Review> list = reviewDao.getAll();

        Assert.assertEquals(2, list.size());
        Assert.assertEquals(SUBJECTID, list.get(0).getSubjectId());
        Assert.assertEquals(SUBJECTID2, list.get(1).getSubjectId());

    }

    @Test
    public void testCreate(){
        jdbcTemplate.execute("INSERT INTO " + Tables.IMG + " VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username, image_id) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");

        Review review = reviewDao.create(
            Review.builder()
                .anonymous(ANONYMOUS)
                .easy(DIFFICULTY)
                .timeDemanding(TIMEDEMANDING)
                .text(TEXT)
                .subjectId(SUBJECTID)
                .userId(USERID)
                .build()
        );

        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "reviews"));
        Assert.assertEquals(USERID, review.getUserId());
        Assert.assertEquals(SUBJECTID, review.getSubjectId());
        Assert.assertEquals(TEXT, review.getText());
    }

    @Test
    public void testDelete(){
        jdbcTemplate.execute("INSERT INTO " + Tables.IMG + " VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username, image_id) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEWS + " (id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID + ", " + USERID + ", '" + SUBJECTID + "', " + SCORE + ", " + DIFFICULTY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")" );

        reviewDao.delete(ID);

        Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "reviews"));
    }

    @Test
    public void testUpdate(){
        jdbcTemplate.execute("INSERT INTO " + Tables.IMG + " VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username, image_id) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEWS + " (id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID + ", " + USERID + ", '" + SUBJECTID + "', " + SCORE + ", " + DIFFICULTY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")" );

        //update is only for the text, easy, timedemanding and anonymous
        Review review = Review.builder()
            .id(ID)
            .userId(USERID)
            .subjectId(SUBJECTID)
            .easy(NEWEASY)
            .timeDemanding(NEWTIMEDEMANDING)
            .text(NEWTEXT)
            .anonymous(NEWANONYMOUS)
            .build();
        reviewDao.update(review);

        String oldQuery = "revtext = '" + TEXT + "' AND useranonymous = " + ANONYMOUS + " AND easy = " + DIFFICULTY + " AND timedemanding = " + TIMEDEMANDING;
        Assert.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviews", oldQuery));
        String query = "revtext = '" + NEWTEXT + "' AND useranonymous = " + NEWANONYMOUS + " AND easy = " + NEWEASY + " AND timedemanding = " + NEWTIMEDEMANDING;
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviews", query));
    }

    @Test
    public void testGetReviewStatBySubject(){
        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEWS + " (idsub, iduser, easy, revtext) VALUES" +
                " ('" + SUBJECTID + "', " + USERID + ", " + DIFFICULTY + ", '" + TEXT + "')");

        Optional<ReviewStats> reviewStats = reviewDao.getReviewStatBySubject(SUBJECTID);
        Optional<ReviewStats> reviewStats2 = reviewDao.getReviewStatBySubject("12.1");

        Assert.assertFalse(reviewStats2.isPresent());
        Assert.assertTrue(reviewStats.isPresent());
        Assert.assertEquals(1, reviewStats.get().getEasyCount());
        Assert.assertEquals(0, reviewStats.get().getMediumCount());
        Assert.assertEquals(0, reviewStats.get().getHardCount());
        Assert.assertEquals(1, reviewStats.get().getReviewCount());
    }

    @Test
    public void testGetReviewStatBySubjectList(){
        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username) VALUES (" + USERID2 + ", '" + EMAIL2 + "', '" + PASSWORD2 + "', '" + USERNAME2 + "')");

        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES (" + SUBJECTID2 + ", '" + NAME2 + "', '" + DEPARTMENT2 + "', " + CREDITS + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEWS + " (id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID + ", " + USERID + ", '" + SUBJECTID + "', " + SCORE + ", " + DIFFICULTY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")" );
        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEWS + "(id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID2 + ", " + USERID2 + ", '" + SUBJECTID2 + "', " + SCORE + ", " + HARD + ", " + NOTTIMEDEMANDING+ ", '" + TEXT + "', " + ANONYMOUS + ")" );

        List<String> idList = new ArrayList<>();
        idList.add(SUBJECTID);
        idList.add(SUBJECTID2);

        List<ReviewStats> list = reviewDao.getReviewStatBySubjectList(idList);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals(SUBJECTID, list.get(0).getIdSub());
        Assert.assertEquals(SUBJECTID2, list.get(1).getIdSub());
        Assert.assertEquals(1, list.get(0).getEasyCount());
        Assert.assertEquals(0, list.get(0).getMediumCount());
        Assert.assertEquals(1, list.get(1).getHardCount());


    }

    @Test
    public void testGetReviewStatMapBySubjectList(){
        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username) VALUES (" + USERID2 + ", '" + EMAIL2 + "', '" + PASSWORD2 + "', '" + USERNAME2 + "')");

        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES (" + SUBJECTID2 + ", '" + NAME2 + "', '" + DEPARTMENT2 + "', " + CREDITS + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEWS + " (id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID + ", " + USERID + ", '" + SUBJECTID + "', " + SCORE + ", " + DIFFICULTY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")" );
        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEWS + " (id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID2 + ", " + USERID2 + ", '" + SUBJECTID2 + "', " + SCORE + ", " + HARD + ", " + NOTTIMEDEMANDING+ ", '" + TEXT + "', " + ANONYMOUS + ")" );

        List<String> idList = new ArrayList<>();
        idList.add(SUBJECTID);
        idList.add(SUBJECTID2);

        Map<String, ReviewStats> map = reviewDao.getReviewStatMapBySubjectList(idList);
        Assert.assertEquals(2, map.size());
        Assert.assertEquals(SUBJECTID, map.get(SUBJECTID).getIdSub());
        Assert.assertEquals(SUBJECTID2, map.get(SUBJECTID2).getIdSub());
        Assert.assertEquals(1, map.get(SUBJECTID).getEasyCount());
        Assert.assertEquals(0, map.get(SUBJECTID).getMediumCount());
        Assert.assertEquals(1, map.get(SUBJECTID2).getHardCount());


    }

    @Test
    public void testDidUserReivewDBTrue(){
        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEWS + " (id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID + ", " + USERID + ", '" + SUBJECTID + "', " + SCORE + ", " + DIFFICULTY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")" );

        Boolean didReview = reviewDao.didUserReviewDB(SUBJECTID, USERID);
        Assert.assertTrue(didReview);
    }

    @Test
    public void testDidUserReivewDBFalse(){
        Boolean didReview = reviewDao.didUserReviewDB(SUBJECTID, USERID);
        Assert.assertFalse(didReview);
    }

    @Test
    public void testVoteReview(){
        jdbcTemplate.execute("INSERT INTO " + Tables.IMG + " VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEWS + " (id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID + ", " + USERID + ", '" + SUBJECTID + "', " + SCORE + ", " + DIFFICULTY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")" );


        reviewDao.voteReview(USERID, ID, VOTE);

        String query = "iduser = " + USERID + " AND idreview = " + ID + " AND vote = " + VOTE;
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviewvote", query) );

    }

    @Test
    public void testDeleteReviewVoteByReviewId(){
        jdbcTemplate.execute("INSERT INTO " + Tables.IMG + " VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username, image_id) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEWS + " (id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID + ", " + USERID + ", '" + SUBJECTID + "', " + SCORE + ", " + DIFFICULTY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")" );
        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEW_VOTES + " VALUES (" + USERID + ", " + ID + ", " + VOTE + ")");


        reviewDao.deleteReviewVoteByReviewId(ID);

        String query = "iduser = " + USERID + " AND idreview = " + ID + " AND vote = " + VOTE;
        Assert.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviewvote", query));
    }

    @Test
    public void testDeleteReviewVote(){
        jdbcTemplate.execute("INSERT INTO " + Tables.IMG + " VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username, image_id) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEWS + " (id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID + ", " + USERID + ", '" + SUBJECTID + "', " + SCORE + ", " + DIFFICULTY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")" );
        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEW_VOTES + " VALUES (" + USERID + ", " + ID + ", " + VOTE + ")");


        reviewDao.deleteReviewVote(USERID, ID);

        String query = "iduser = " + USERID + " AND idreview = " + ID + " AND vote = " + VOTE;
        Assert.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviewvote", query));
    }

    @Test
    public void testUserVotedOnReviewTrue(){
        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "')");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEWS + " (id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID + ", " + USERID + ", '" + SUBJECTID + "', " + SCORE + ", " + DIFFICULTY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")" );
        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEW_VOTES + " VALUES (" + USERID + ", " + ID + ", " + VOTE + ")");

        Boolean bool = reviewDao.userVotedOnReview(USERID, ID);

        Assert.assertTrue(bool);
    }
    @Test
    public void testUserVotedOnReviewFalse(){
        Boolean bool = reviewDao.userVotedOnReview(USERID, ID);

        Assert.assertFalse(bool);
    }

    @Test
    public void testUpdateVoteOnReview(){
        jdbcTemplate.execute("INSERT INTO " + Tables.IMG + " VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username, image_id) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEWS + " (id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID + ", " + USERID + ", '" + SUBJECTID + "', " + SCORE + ", " + DIFFICULTY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")" );
        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEW_VOTES + " VALUES (" + USERID + ", " + ID + ", " + VOTE + ")");


        reviewDao.updateVoteOnReview(USERID, ID, NEWVOTE);

        String query = "iduser = " + USERID + " AND idreview = " + ID + " AND vote = " + NEWVOTE;
        String queryNotExits = "iduser = " + USERID + " AND idreview = " + ID + " AND vote = " + VOTE;
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviewvote", query));
        Assert.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviewvote", queryNotExits));


    }

    @Test
    public void testUserReviewVoteByIdSubAndIdUser(){
        jdbcTemplate.execute("INSERT INTO " + Tables.IMG + " VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username, image_id) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEWS + " (id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID + ", " + USERID + ", '" + SUBJECTID + "', " + SCORE + ", " + DIFFICULTY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")" );
        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEW_VOTES + " VALUES (" + USERID + ", " + ID + ", " + VOTE + ")");

        Map<Long, Integer> map = reviewDao.userReviewVoteByIdSubAndIdUser(SUBJECTID, USERID);

        Assert.assertEquals(VOTE, map.get(ID).intValue());
    }

    @Test
    public void userReviewVoteByIdUser(){
        jdbcTemplate.execute("INSERT INTO " + Tables.IMG + " VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username, image_id) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEWS + " (id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID + ", " + USERID + ", '" + SUBJECTID + "', " + SCORE + ", " + DIFFICULTY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")" );
        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEW_VOTES + " VALUES (" + USERID + ", " + ID + ", " + VOTE + ")");

        Map<Long, Integer> map = reviewDao.userReviewVoteByIdUser(USERID);

        Assert.assertEquals(VOTE, map.get(ID).intValue());
    }

    @Test
    public void testGetAllUserReviewsWithSubjectName(){
        jdbcTemplate.execute("INSERT INTO " + Tables.IMG + " VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username, image_id) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES (" + SUBJECTID2 + ", '" + NAME2 + "', '" + DEPARTMENT2 + "', " + CREDITS + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEWS + " (id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID + ", " + USERID + ", '" + SUBJECTID + "', " + SCORE + ", " + DIFFICULTY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")" );
        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEWS + " (id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID2 + ", " + USERID + ", '" + SUBJECTID2 + "', " + SCORE + ", " + HARD + ", " + AVERAGETIMEDEMANDING + ", '" + NEWTEXT + "', " + ANONYMOUS + ")" );
        Map<String,String> params = new HashMap<>();
        params.put(PARAM_PAGE_NUM,PAGE_ZERO);
        List<Review> list = reviewDao.getAllUserReviewsWithSubjectName(USERID,params);

        Assert.assertEquals(2, list.size());
        Assert.assertEquals(NAME, list.get(0).getSubjectName());
        Assert.assertEquals(NAME2, list.get(1).getSubjectName());
    }

    @Test
    public void testGetAllSubjectReviewsWithUsername(){
        jdbcTemplate.execute("INSERT INTO " + Tables.IMG + " VALUES (" + IMAGEID + ", " + IMAGE + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username, image_id) VALUES (" + USERID + ", '" + EMAIL + "', '" + PASSWORD + "', '" + USERNAME + "', " + IMAGEID + ")");
        jdbcTemplate.execute("INSERT INTO " + Tables.USERS + " (id, email, pass, username, image_id) VALUES (" + USERID2 + ", '" + EMAIL2 + "', '" + PASSWORD2 + "', '" + USERNAME2 + "', " + IMAGEID + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.SUBJECTS + " (id, subname, department, credits) VALUES (" + SUBJECTID + ", '" + NAME + "', '" + DEPARTMENT + "', " + CREDITS + ")");

        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEWS + " (id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID + ", " + USERID + ", '" + SUBJECTID + "', " + SCORE + ", " + DIFFICULTY + ", " + TIMEDEMANDING + ", '" + TEXT + "', " + ANONYMOUS + ")" );
        jdbcTemplate.execute("INSERT INTO " + Tables.REVIEWS + " (id, iduser, idsub, score, easy, timedemanding, revtext, useranonymous) " +
                "VALUES (" + ID2 + ", " + USERID2 + ", '" + SUBJECTID + "', " + SCORE + ", " + HARD + ", " + AVERAGETIMEDEMANDING + ", '" + NEWTEXT + "', " + ANONYMOUS + ")" );

        List<Review> list = reviewDao.getAllSubjectReviewsWithUsername(SUBJECTID, new HashMap<>());

        Assert.assertEquals(2, list.size());
        Assert.assertEquals(USERNAME, list.get(0).getUsername());
        Assert.assertEquals(USERNAME2, list.get(1).getUsername());

    }
}
