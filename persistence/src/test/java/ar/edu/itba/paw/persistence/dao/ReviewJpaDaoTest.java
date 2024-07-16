package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewVote;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.*;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.mock.PageMockData;
import ar.edu.itba.paw.persistence.mock.ReviewMockData;
import ar.edu.itba.paw.persistence.mock.SubjectMockData;
import ar.edu.itba.paw.persistence.mock.UserMockData;
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


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ReviewJpaDaoTest {
    private final ReviewVote testReviewVote = new ReviewVote(UserMockData.getUser1(), ReviewMockData.getReview2(), ReviewVoteType.UPVOTE);

    @Autowired
    private DataSource dataSource;
    @PersistenceContext
    private EntityManager em;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReviewJpaDao reviewJpaDao;


    @Before
    public void setup() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("ALTER SEQUENCE reviews_id_seq RESTART WITH 3;");
    }

    @Rollback
    @Test
    public void testFindById() {
        final Optional<Review> review = reviewJpaDao.findById(1);

        assertTrue(review.isPresent());;
        assertEquals(ReviewMockData.REV1_ID, review.get().getId());
        assertEquals(ReviewMockData.REV1_DIFFICULTY, review.get().getDifficulty());
        assertEquals(ReviewMockData.REV1_TIMEDEMAND, review.get().getTimeDemanding());
        assertEquals(ReviewMockData.REV1_TEXT, review.get().getText());
    }

    @Rollback
    @Test
    public void testCreate() {
        final Review reviewToCreate =  Review.builder().user(UserMockData.getUser1()).subject(SubjectMockData.getSubject2()).difficulty(Difficulty.HARD).timeDemanding(TimeDemanding.HIGH).text("Very Hard").build();
        final Review newReview = reviewJpaDao.create(Review.builderFrom(reviewToCreate));
        em.flush();

        assertEquals(reviewToCreate.getDifficulty(), newReview.getDifficulty());
        assertEquals(reviewToCreate.getTimeDemanding(), newReview.getTimeDemanding());
        assertEquals(reviewToCreate.getText(), newReview.getText());
        assertEquals(reviewToCreate.getSubject(), newReview.getSubject());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate,
                "reviews",
                "id = " + newReview.getId()
        ));
    }

    @Rollback
    @Test
    public void testUpdate() {
        final Review.Builder reviewUpdate = Review.builderFrom(ReviewMockData.getReview2()).text("Maso").difficulty(Difficulty.MEDIUM);
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
        final boolean didReview = reviewJpaDao.didUserReview(SubjectMockData.getSubject1(), UserMockData.getUser1());

        assertTrue(didReview);
    }

    @Rollback
    @Test
    public void testGetAllUserReviews() {
        final List<Review> userReviews = reviewJpaDao.reviewSearch(UserMockData.getUser2(), null, UserMockData.getUser2().getId(), PageMockData.DEFAULT_PAGE, PageMockData.DEFAULT_ORDER_REVIEW, PageMockData.DEFAULT_DIR);

        assertEquals(1, userReviews.size());
        assertTrue(userReviews.contains(ReviewMockData.getReview2()));
    }

    @Rollback
    @Test
    public void testGetAllSubjectReviews() {
        final List<Review> subjectReviews = reviewJpaDao.reviewSearch(UserMockData.getUser2(), SubjectMockData.getSubject2().getId(), null, PageMockData.DEFAULT_PAGE, PageMockData.DEFAULT_ORDER_REVIEW, PageMockData.DEFAULT_DIR);

        assertEquals(1, subjectReviews.size());
        assertTrue(subjectReviews.contains(ReviewMockData.getReview2()));
    }
}
