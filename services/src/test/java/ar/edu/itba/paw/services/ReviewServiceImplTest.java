package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.Difficulty;
import ar.edu.itba.paw.models.constants.TimeDemanding;
import ar.edu.itba.paw.persistence.dao.ReviewDao;
import ar.edu.itba.paw.services.exceptions.NoGrantedPermissionException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

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

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    public void testDidUserReviewTrue(){
        Review review1 = Review.builder()
            .id(1)
            .userId(USERID)
            .subjectId("31.08")
            .easy(Difficulty.MEDIUM)
            .timeDemanding(TimeDemanding.MEDIUM)
            .text("Resena")
            .anonymous(false)
            .build();

        Review review2 = Review.builder()
            .id(2)
            .userId(USERID+1)
            .subjectId("31.08")
            .easy(Difficulty.MEDIUM)
            .timeDemanding(TimeDemanding.MEDIUM)
            .text("Resena")
            .anonymous(false)
            .build();

        Review review3 = Review.builder()
            .id(3)
            .userId(USERID+2)
            .subjectId("31.08")
            .easy(Difficulty.MEDIUM)
            .timeDemanding(TimeDemanding.MEDIUM)
            .text("Resena")
            .anonymous(false)
            .build();

        List<Review> reviews = new ArrayList<>();
        reviews.add(review1);
        reviews.add(review2);
        reviews.add(review3);
        User user =  User.builder(EMAIL, PASSWORD, USERNAME)
            .id(USERID)
            .build();

        Assert.assertTrue(reviewService.didUserReview(reviews, user));

    }
    @Test
    public void testDidUserReviewFalse(){
        Review review1 = Review.builder()
            .id(1)
            .userId(USERID-1)
            .subjectId("31.08")
            .easy(Difficulty.MEDIUM)
            .timeDemanding(TimeDemanding.MEDIUM)
            .text("Resena")
            .anonymous(false)
            .build();

        Review review2 = Review.builder()
            .id(2)
            .userId(USERID+1)
            .subjectId("31.08")
            .easy(Difficulty.MEDIUM)
            .timeDemanding(TimeDemanding.MEDIUM)
            .text("Resena")
            .anonymous(false)
            .build();

        Review review3 = Review.builder()
            .id(3)
            .userId(USERID+2)
            .subjectId("31.08")
            .easy(Difficulty.MEDIUM)
            .timeDemanding(TimeDemanding.MEDIUM)
            .text("Resena")
            .anonymous(false)
            .build();

        List<Review> reviews = new ArrayList<>();
        reviews.add(review1);
        reviews.add(review2);
        reviews.add(review3);

        User user = User.builder(EMAIL, PASSWORD, USERNAME)
            .id(USERID)
            .build();

        Assert.assertFalse(reviewService.didUserReview(reviews, user));

    }

    @Test
    public void testGetReviewStatBySubject(){
        Map<String, ReviewStats> map = new HashMap<>();
        ReviewStats reviewStats = new ReviewStats(ID1, COUNT, 2, 1, 0, 3,0,0);
        map.put(ID1, reviewStats);
        List<String> subjects = new ArrayList<>();
        subjects.add(ID1);
        subjects.add(ID2);
        when(reviewDao.getReviewStatMapBySubjectList(subjects)).thenReturn(map);

        //Test to see if the method in service passes the subjectId correctly and
        // returns an empty ReviewStatistic in case it is missing
        Subject subject = new Subject(ID1, "sistemas","departamento", 3, new HashSet<>(), new HashSet<>(), new HashSet<>());
        Subject subject2 = new Subject(ID2, "otra","departamento", 3, new HashSet<>(), new HashSet<>(), new HashSet<>());
        List<Subject> list = new ArrayList<>();
        list.add(subject);
        list.add(subject2);
        Map<String, ReviewStats> toRet = reviewService.getReviewStatMapBySubjectList(list);

        Assert.assertEquals(toRet.size(), 2);
        Assert.assertEquals(toRet.get(ID1).getReviewCount(), COUNT);
        Assert.assertEquals(toRet.get(ID2).getReviewCount(), 0);

    }
    @Test(expected = NoGrantedPermissionException.class)
    public void testCheckAuthFalse() throws NoGrantedPermissionException {
        Review review1 = Review.builder()
            .id(1)
            .userId(USERID)
            .subjectId("31.08")
            .easy(Difficulty.MEDIUM)
            .timeDemanding(TimeDemanding.MEDIUM)
            .text("Resena")
            .anonymous(false)
            .build();

        User user = User.builder(EMAIL, PASSWORD, USERNAME)
            .id(ID+1)
            .build();

        reviewService.deleteReview(review1, user, false);
    }


}
