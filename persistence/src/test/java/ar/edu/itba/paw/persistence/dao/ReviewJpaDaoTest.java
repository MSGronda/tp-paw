package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewVote;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.*;
import ar.edu.itba.paw.models.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ReviewJpaDaoTest {
    private final User testUser = User.builder().id(1).build();
    private final User testUser2 = User.builder().id(2).build();
    private final Subject testSubject = Subject.builder().id("11.15").build();
    private final Subject testSubject2 = Subject.builder().id("11.16").build();

    private final Review testReview = Review.builder().id(1).user(testUser).subject(testSubject).difficulty(Difficulty.EASY).timeDemanding(TimeDemanding.LOW).text("Very Easy").anonymous(false).build();
    private final Review testReview2 = Review.builder().user(testUser).subject(testSubject2).difficulty(Difficulty.HARD).timeDemanding(TimeDemanding.HIGH).text("Very Hard").build();
    private final Review testReview3 = Review.builder().id(2).user(testUser2).subject(testSubject2).difficulty(Difficulty.EASY).timeDemanding(TimeDemanding.MEDIUM).text("Real Easy").build();

    private final ReviewVote testReviewVote = new ReviewVote(testUser, testReview3, ReviewVoteType.UPVOTE);

    private static final int DEFAULT_PAGE = 1;
    private static final ReviewOrderField DEFAULT_ORDER = ReviewOrderField.DIFFICULTY;
    private  static final OrderDir DEFAULT_DIR = OrderDir.ASCENDING;

    @Autowired
    private DataSource dataSource;
    @PersistenceContext
    private EntityManager em;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReviewJpaDao reviewJpaDao;
//    @Autowired
//    private UserJpaDao userJpaDao;

    @Before
    public void setup() {
        jdbcTemplate = new JdbcTemplate(dataSource);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, "reviews");
    }

    @Rollback
    @Test
    public void testFindById() {
        final Optional<Review> review = reviewJpaDao.findById(1);

        assertTrue(review.isPresent());;
        assertEquals(testReview.getId(), review.get().getId());
        assertEquals(testReview.getDifficulty(), review.get().getDifficulty());
        assertEquals(testReview.getTimeDemanding(), review.get().getTimeDemanding());
        assertEquals(testReview.getText(), review.get().getText());
    }

    @Rollback
    @Test
    public void testCreate() {

        final Review newReview = reviewJpaDao.create(Review.builderFrom(testReview2));

        assertEquals(testReview2.getDifficulty(), newReview.getDifficulty());
        assertEquals(testReview2.getTimeDemanding(), newReview.getTimeDemanding());
        assertEquals(testReview2.getText(), newReview.getText());
        assertEquals(testReview2.getSubject(), newReview.getSubject());
    }

    @Rollback
    @Test
    public void testUpdate() {
        final Review.Builder reviewUpdate = Review.builderFrom(testReview3).text("Maso").difficulty(Difficulty.MEDIUM);
        final Review updatedReview = reviewJpaDao.update(reviewUpdate);

        assertEquals(reviewUpdate.getId(), updatedReview.getId());
        assertEquals(reviewUpdate.getDifficulty(), updatedReview.getDifficulty());
        assertEquals(reviewUpdate.getTimeDemanding(), updatedReview.getTimeDemanding());
        assertEquals(reviewUpdate.getText(), updatedReview.getText());
        assertEquals(reviewUpdate.getSubject(), updatedReview.getSubject());
    }

    @Rollback
    @Test
    public void testDidUserReview() {
        final boolean didReview = reviewJpaDao.didUserReview(testSubject, testUser);

        assertTrue(didReview);
    }

    @Rollback
    @Test
    public void testReviewVoting() {
//        final Review review = reviewJpaDao.findById(2).orElseThrow(ReviewNotFoundException::new);
//        final User user = userJpaDao.findById(2).orElseThrow(UserNotFoundException::new);
//        final ReviewVote reviewVote = reviewJpaDao.voteReview(user, review, ReviewVoteType.UPVOTE);
//
//        assertEquals(testReviewVote.getReview(), reviewVote.getReview());
//        assertEquals(testReviewVote.getUser(), reviewVote.getUser());
//        assertEquals(testReviewVote.getVote(), reviewVote.getVote());
    }

    @Rollback
    @Test
    public void testGetAllUserReviews() {
        final List<Review> userReviews = reviewJpaDao.getAllUserReviews(testUser2, DEFAULT_PAGE, DEFAULT_ORDER, DEFAULT_DIR);

        assertEquals(1, userReviews.size());
        assertTrue(userReviews.contains(testReview3));
    }

    @Rollback
    @Test
    public void testGetAllSubjectReviews() {
        final List<Review> subjectReviews = reviewJpaDao.getAllSubjectReviews(testSubject2, DEFAULT_PAGE, DEFAULT_ORDER, DEFAULT_DIR);

        assertEquals(1, subjectReviews.size());
        assertTrue(subjectReviews.contains(testReview3));
    }
}
