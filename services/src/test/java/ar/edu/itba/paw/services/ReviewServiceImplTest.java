package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.*;
import ar.edu.itba.paw.models.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.models.exceptions.UnauthorizedException;
import ar.edu.itba.paw.persistence.dao.ReviewDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReviewServiceImplTest {
    private static final long userId = 1;
    private static final User testUser = User.builder().id(userId).build();
    private static final Subject testSubject = Subject.builder().id("11.15").build();

    private static final Review testReview = Review.builder()
            .id(1)
            .user(testUser)
            .subject(testSubject)
            .difficulty(Difficulty.EASY)
            .timeDemanding(TimeDemanding.LOW)
            .text("It was easy")
            .anonymous(false)
            .build();

    // Pagination and order
    private static final int defaultPage = 1;
    private static final String defaultOrderBy = "difficulty";
    private static final String defaultDir = "asc";

    @Mock
    private ReviewDao reviewDao;
    @Mock
    private UserService userService;
    @Mock
    private SubjectService subjectService;
    @Mock
    private AuthUserService authUserService;
    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    public void testGetReviewByUserId() {
        when(userService.findById(userId)).thenReturn(Optional.of(testUser));
        when(reviewDao.getTotalPagesForUserReviews(testUser)).thenReturn(1);
        when(reviewDao.getAllUserReviews(testUser, defaultPage, ReviewOrderField.parse(defaultOrderBy), OrderDir.parse(defaultDir)))
                .thenReturn(new ArrayList<>(Collections.singletonList(testReview)));

        final List<Review> userReviews = reviewService.get(userId, null, defaultPage, defaultOrderBy, defaultDir);

        assertEquals(1, userReviews.size());
        assertEquals(userReviews, Collections.singletonList(testReview));
    }

    @Test
    public void testGetReviewBySubjectId() {
        when(subjectService.findById(testSubject.getId())).thenReturn(Optional.of(testSubject));
        when(reviewDao.getTotalPagesForSubjectReviews(testSubject)).thenReturn(1);
        when(reviewDao.getAllSubjectReviews(testSubject, defaultPage, ReviewOrderField.parse(defaultOrderBy), OrderDir.parse(defaultDir)))
                .thenReturn(new ArrayList<>(Collections.singletonList(testReview)));

        final List<Review> subjectReviews = reviewService.get(null, testSubject.getId(), defaultPage, defaultOrderBy, defaultDir);

        assertEquals(1, subjectReviews.size());
        assertEquals(subjectReviews, Collections.singletonList(testReview));
    }

    @Test
    public void testVoteReview() {
        when(reviewService.findById(testReview.getId())).thenReturn(Optional.of(testReview));
        when(authUserService.getCurrentUser()).thenReturn(testUser);
        when(reviewDao.voteReview(testUser, testReview, ReviewVoteType.UPVOTE)).thenReturn(new ReviewVote(testUser, testReview, ReviewVoteType.UPVOTE));

        final ReviewVote reviewVote = reviewService.voteReview(testReview.getId(), ReviewVoteType.UPVOTE);

        assertEquals(reviewVote, new ReviewVote(testUser, testReview, ReviewVoteType.UPVOTE));
    }

    @Test(expected = ReviewNotFoundException.class)
    public void testVoteOnInvalidReview() {
        when(reviewService.findById(testReview.getId())).thenReturn(Optional.empty());

        final ReviewVote reviewVote = reviewService.voteReview(testReview.getId(), ReviewVoteType.DOWNVOTE);
        Assert.fail("Should have thrown ReviewNotFoundException");
    }

    @Test
    public void testUpdateReview() {
        final boolean newAnonymous = false;
        final Difficulty newDifficulty = Difficulty.HARD;
        final String newText = "Very hard!";
        final Review newReview = Review.builderFrom(testReview).anonymous(newAnonymous).difficulty(newDifficulty).text(newText).build();
        final Review.Builder newReviewBuilder = Review.builderFrom(newReview);
        when(authUserService.getCurrentUser()).thenReturn(testUser);
        when(reviewDao.update(newReviewBuilder)).thenReturn(newReview);

        final Review updatedReview = reviewService.update(newReviewBuilder);

        assertEquals(updatedReview, newReview);
    }

    @Test(expected = UnauthorizedException.class)
    public void testUpdateReviewUnauthorized() {
        when(authUserService.getCurrentUser()).thenReturn(User.builder().id(2).build());

        final Review updatedReview = reviewService.update(Review.builderFrom(testReview));
        Assert.fail("Should have thrown UnauthorizedException");
    }
}
