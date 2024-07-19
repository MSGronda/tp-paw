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
        when(reviewDao.update(newReviewBuilder)).thenReturn(newReview);

        final Review updatedReview = reviewService.update(newReviewBuilder, testUser);

        assertEquals(updatedReview, newReview);
    }

    @Test(expected = UnauthorizedException.class)
    public void testUpdateReviewUnauthorized() {
        final User user = User.builder().id(2).build();
        reviewService.update(Review.builderFrom(testReview), user);
        Assert.fail("Should have thrown UnauthorizedException");
    }
}
