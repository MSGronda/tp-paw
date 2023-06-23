package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewStats;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.Difficulty;
import ar.edu.itba.paw.models.enums.TimeDemanding;
import ar.edu.itba.paw.models.exceptions.UnauthorizedException;
import ar.edu.itba.paw.persistence.dao.ReviewDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReviewServiceImplTest {

    private final long ID = 1;
    private final String ID1 = "31.08";
    private final String ID2 = "72.33";
    private final int COUNT = 3;

    private static final int USERID = 1;
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";


    @Mock
    private ReviewDao reviewDao;

    @Mock
    private AuthUserService authUserService;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    public void testDidUserReviewTrue(){
        final User user =  User.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .username(USERNAME)
                .id(USERID)
                .build();

        final Subject subject = Subject.builder()
                .id(ID1)
                .name("name")
                .build();

        when(reviewDao.didUserReview(subject, user)).thenReturn(true);
        assertTrue(reviewService.didUserReview(subject, user));

        when(reviewDao.didUserReview(subject, user)).thenReturn(false);
        assertFalse(reviewService.didUserReview(subject, user));
    }

    @Test(expected = UnauthorizedException.class)
    public void testCheckAuthFalse() throws UnauthorizedException {
        final User user1 = User.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .username(USERNAME)
                .id(ID)
                .build();

        final User user2 = User.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .username(USERNAME)
                .id(ID+1)
                .build();

        final Review review = Review.builder()
            .id(1)
            .user(user1)
            .difficulty(Difficulty.MEDIUM)
            .timeDemanding(TimeDemanding.MEDIUM)
            .text("review")
            .anonymous(false)
            .build();

        when(authUserService.getCurrentUser()).thenReturn(user2);

        reviewService.delete(review);
    }
}
