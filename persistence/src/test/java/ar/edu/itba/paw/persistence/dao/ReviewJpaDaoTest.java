package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewVote;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.*;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.Assert.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ReviewJpaDaoTest {
    private final static String TEXT = "text";
    private final static TimeDemanding TIME = TimeDemanding.HIGH;
    private final static Difficulty DIFF = Difficulty.HARD;
    private final static boolean ANON = false;

    private final static String TEXT_2 = "text2";
    private final static TimeDemanding TIME_2 = TimeDemanding.MEDIUM;
    private final static Difficulty DIFF_2 = Difficulty.MEDIUM;
    private final static boolean ANON_2 = true;

    private final static String USER_EMAIL = "email";
    private final static String USER_NAME = "name";
    private final static String USER_PASS = "pass";

    private final static String SUB_ID = "75.40";
    private final static String SUB_NAME = "subject";
    private final static String SUB_DEPT = "department";
    private final static int SUB_CREDITS = 6;



    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ReviewJpaDao reviewJpaDao;

    @Before
    public void clear() {
        em.createQuery("DELETE FROM Review").executeUpdate();
        em.createQuery("DELETE FROM Subject").executeUpdate();
    }

    @Test
    public void create() {
        final Subject subject = Subject.builder()
                .id(SUB_ID)
                .name(SUB_NAME)
                .credits(SUB_CREDITS)
                .department(SUB_DEPT)
                .build();
        em.persist(subject);

        final User user = User.builder()
                .email(USER_EMAIL)
                .username(USER_NAME)
                .password(USER_PASS)
                .confirmed(true)
                .build();

        em.persist(user);

        final Review actual = reviewJpaDao.create(Review.builder()
                .subject(subject)
                .user(user)
                .text(TEXT)
                .difficulty(DIFF)
                .anonymous(ANON)
                .timeDemanding(TIME)
        );

        assertNotNull(actual);
        assertEquals(subject, actual.getSubject());
        assertEquals(user, actual.getUser());
        assertEquals(TEXT, actual.getText());
        assertEquals(DIFF, actual.getDifficulty());
        assertEquals(ANON, actual.isAnonymous());
        assertEquals(TIME, actual.getTimeDemanding());
    }

    @Test
    public void update() {
        final Subject subject = Subject.builder()
                .id(SUB_ID)
                .name(SUB_NAME)
                .credits(SUB_CREDITS)
                .department(SUB_DEPT)
                .build();
        em.persist(subject);

        final User user = User.builder()
                .email(USER_EMAIL)
                .username(USER_NAME)
                .password(USER_PASS)
                .confirmed(true)
                .build();

        em.persist(user);

        final Review review = Review.builder()
                .subject(subject)
                .user(user)
                .text(TEXT)
                .difficulty(DIFF)
                .anonymous(ANON)
                .timeDemanding(TIME)
                .build();

        em.persist(review);

        final Review actual = reviewJpaDao.update(
                Review.builderFrom(review)
                        .text(TEXT_2)
        );

        assertNotNull(actual);
        assertEquals(subject, actual.getSubject());
        assertEquals(user, actual.getUser());
        assertEquals(TEXT_2, actual.getText());
        assertEquals(DIFF, actual.getDifficulty());
        assertEquals(ANON, actual.isAnonymous());
        assertEquals(TIME, actual.getTimeDemanding());
    }

    @Test
    public void delete() {
        final Subject subject = Subject.builder()
                .id(SUB_ID)
                .name(SUB_NAME)
                .credits(SUB_CREDITS)
                .department(SUB_DEPT)
                .build();

        em.persist(subject);

        final User user = User.builder()
                .email(USER_EMAIL)
                .username(USER_NAME)
                .password(USER_PASS)
                .confirmed(true)
                .build();

        em.persist(user);

        final Review review = Review.builder()
                .subject(subject)
                .user(user)
                .text(TEXT)
                .difficulty(DIFF)
                .anonymous(ANON)
                .timeDemanding(TIME)
                .build();

        em.persist(review);

        reviewJpaDao.delete(review);

        assertEquals(0, em.createQuery("from Review").getResultList().size());
    }

    @Test
    public void findById() {
        final Subject subject = Subject.builder()
                .id(SUB_ID)
                .name(SUB_NAME)
                .credits(SUB_CREDITS)
                .department(SUB_DEPT)
                .build();

        em.persist(subject);

        final User user = User.builder()
                .email(USER_EMAIL)
                .username(USER_NAME)
                .password(USER_PASS)
                .confirmed(true)
                .build();

        em.persist(user);

        final Review review = Review.builder()
                .subject(subject)
                .user(user)
                .text(TEXT)
                .difficulty(DIFF)
                .anonymous(ANON)
                .timeDemanding(TIME)
                .build();

        em.persist(review);

        final Review actual = reviewJpaDao.findById(review.getId()).get();

        assertEquals(subject, actual.getSubject());
        assertEquals(user, actual.getUser());
        assertEquals(TEXT, actual.getText());
        assertEquals(DIFF, actual.getDifficulty());
        assertEquals(ANON, actual.isAnonymous());
        assertEquals(TIME, actual.getTimeDemanding());
    }

    @Test
    public void voteReview() {
        final Subject subject = Subject.builder()
                .id(SUB_ID)
                .name(SUB_NAME)
                .credits(SUB_CREDITS)
                .department(SUB_DEPT)
                .build();

        em.persist(subject);

        final User user = User.builder()
                .email(USER_EMAIL)
                .username(USER_NAME)
                .password(USER_PASS)
                .confirmed(true)
                .build();

        em.persist(user);

        Review review = Review.builder()
                .subject(subject)
                .user(user)
                .text(TEXT)
                .difficulty(DIFF)
                .anonymous(ANON)
                .timeDemanding(TIME)
                .build();

        em.persist(review);

        final ReviewVote actual = reviewJpaDao.voteReview(user, review, ReviewVoteType.UPVOTE).get();

        assertEquals(ReviewVoteType.UPVOTE, actual.getVote());
        assertEquals(user, actual.getUser());
        assertEquals(review, actual.getReview());

        em.flush();
        em.clear();
        review = em.find(Review.class, review.getId());
        assertEquals(1, review.getUpvotes());
    }

    @Test
    public void didUserVote() {
        final Subject subject = Subject.builder()
                .id(SUB_ID)
                .name(SUB_NAME)
                .credits(SUB_CREDITS)
                .department(SUB_DEPT)
                .build();

        em.persist(subject);

        final User user = User.builder()
                .email(USER_EMAIL)
                .username(USER_NAME)
                .password(USER_PASS)
                .confirmed(true)
                .build();

        em.persist(user);

        final Review review = Review.builder()
                .subject(subject)
                .user(user)
                .text(TEXT)
                .difficulty(DIFF)
                .anonymous(ANON)
                .timeDemanding(TIME)
                .build();

        em.persist(review);

        final Review review2 = Review.builder()
                .subject(subject)
                .user(user)
                .text(TEXT_2)
                .difficulty(DIFF)
                .anonymous(ANON)
                .timeDemanding(TIME)
                .build();

        em.persist(review2);

        reviewJpaDao.voteReview(user, review, ReviewVoteType.UPVOTE);

        assertTrue(reviewJpaDao.didUserVote(user, review));
        assertFalse(reviewJpaDao.didUserVote(user, review2));
    }

    @Test
    public void didUserReview() {
        final Subject subject = Subject.builder()
                .id(SUB_ID)
                .name(SUB_NAME)
                .credits(SUB_CREDITS)
                .department(SUB_DEPT)
                .build();

        em.persist(subject);

        final Subject subject2 = Subject.builder()
                .id(SUB_ID+1)
                .name(SUB_NAME)
                .credits(SUB_CREDITS)
                .department(SUB_DEPT)
                .build();

        em.persist(subject2);


        final User user = User.builder()
                .email(USER_EMAIL)
                .username(USER_NAME)
                .password(USER_PASS)
                .confirmed(true)
                .build();

        em.persist(user);

        final Review review = Review.builder()
                .subject(subject)
                .user(user)
                .text(TEXT)
                .difficulty(DIFF)
                .anonymous(ANON)
                .timeDemanding(TIME)
                .build();

        em.persist(review);

        assertTrue(reviewJpaDao.didUserReview(subject, user));
        assertFalse(reviewJpaDao.didUserReview(subject2, user));
    }

    @Test
    public void getAllSubjectReviews() {
        final Subject subject = Subject.builder()
                .id(SUB_ID)
                .name(SUB_NAME)
                .credits(SUB_CREDITS)
                .department(SUB_DEPT)
                .build();

        em.persist(subject);

        final User user = User.builder()
                .email(USER_EMAIL)
                .username(USER_NAME)
                .password(USER_PASS)
                .confirmed(true)
                .build();

        em.persist(user);

        final Review review = Review.builder()
                .subject(subject)
                .user(user)
                .text(TEXT)
                .difficulty(DIFF)
                .anonymous(ANON)
                .timeDemanding(TIME)
                .build();

        em.persist(review);

        final Review review2 = Review.builder()
                .subject(subject)
                .user(user)
                .text(TEXT_2)
                .difficulty(DIFF_2)
                .anonymous(ANON_2)
                .timeDemanding(TIME_2)
                .build();

        em.persist(review2);

        final List<Review> actual = reviewJpaDao.getAllSubjectReviews(subject, 1, ReviewOrderField.DIFFICULTY, OrderDir.ASCENDING);

        assertEquals(2, actual.size());
        assertTrue(actual.contains(review));
        assertTrue(actual.contains(review2));
    }

    @Test
    public void getAllUserReviews() {
        final Subject subject = Subject.builder()
                .id(SUB_ID)
                .name(SUB_NAME)
                .credits(SUB_CREDITS)
                .department(SUB_DEPT)
                .build();

        em.persist(subject);

        final Subject subject2 = Subject.builder()
                .id(SUB_ID+1)
                .name(SUB_NAME)
                .credits(SUB_CREDITS)
                .department(SUB_DEPT)
                .build();

        em.persist(subject2);

        final User user = User.builder()
                .email(USER_EMAIL)
                .username(USER_NAME)
                .password(USER_PASS)
                .confirmed(true)
                .build();

        em.persist(user);

        final Review review = Review.builder()
                .subject(subject)
                .user(user)
                .text(TEXT)
                .difficulty(DIFF)
                .anonymous(ANON)
                .timeDemanding(TIME)
                .build();

        em.persist(review);

        final Review review2 = Review.builder()
                .subject(subject2)
                .user(user)
                .text(TEXT_2)
                .difficulty(DIFF_2)
                .anonymous(ANON_2)
                .timeDemanding(TIME_2)
                .build();

        em.persist(review2);

        final List<Review> actual = reviewJpaDao.getAllUserReviews(user, 1, ReviewOrderField.DIFFICULTY, OrderDir.ASCENDING);

        assertEquals(2, actual.size());
        assertTrue(actual.contains(review));
        assertTrue(actual.contains(review2));
    }

    @Test
    public void getTotalPagesForUserReviews() {
        final Subject subject = Subject.builder()
                .id(SUB_ID)
                .name(SUB_NAME)
                .credits(SUB_CREDITS)
                .department(SUB_DEPT)
                .build();

        em.persist(subject);

        final Subject subject2 = Subject.builder()
                .id(SUB_ID+1)
                .name(SUB_NAME)
                .credits(SUB_CREDITS)
                .department(SUB_DEPT)
                .build();

        em.persist(subject2);

        final User user = User.builder()
                .email(USER_EMAIL)
                .username(USER_NAME)
                .password(USER_PASS)
                .confirmed(true)
                .build();

        em.persist(user);

        final Review review = Review.builder()
                .subject(subject)
                .user(user)
                .text(TEXT)
                .difficulty(DIFF)
                .anonymous(ANON)
                .timeDemanding(TIME)
                .build();

        em.persist(review);

        final Review review2 = Review.builder()
                .subject(subject2)
                .user(user)
                .text(TEXT_2)
                .difficulty(DIFF_2)
                .anonymous(ANON_2)
                .timeDemanding(TIME_2)
                .build();

        em.persist(review2);

        final int actual = reviewJpaDao.getTotalPagesForUserReviews(user);

        assertEquals(1, actual);
    }

    @Test
    public void getTotalPagesForSubjectReviews() {
        final Subject subject = Subject.builder()
                .id(SUB_ID)
                .name(SUB_NAME)
                .credits(SUB_CREDITS)
                .department(SUB_DEPT)
                .build();

        em.persist(subject);

        final User user = User.builder()
                .email(USER_EMAIL)
                .username(USER_NAME)
                .password(USER_PASS)
                .confirmed(true)
                .build();

        em.persist(user);

        final Review review = Review.builder()
                .subject(subject)
                .user(user)
                .text(TEXT)
                .difficulty(DIFF)
                .anonymous(ANON)
                .timeDemanding(TIME)
                .build();

        em.persist(review);

        final Review review2 = Review.builder()
                .subject(subject)
                .user(user)
                .text(TEXT_2)
                .difficulty(DIFF_2)
                .anonymous(ANON_2)
                .timeDemanding(TIME_2)
                .build();

        em.persist(review2);

        assertEquals(1, reviewJpaDao.getTotalPagesForSubjectReviews(subject));
    }
}
