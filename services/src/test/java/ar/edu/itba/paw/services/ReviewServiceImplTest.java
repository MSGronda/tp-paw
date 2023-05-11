package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewStatistic;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.ReviewDao;
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
    public void TestDidUserReviewTrue(){
        Review review1 = new Review(1, USERID, "31.08", 1, 1, "Resena", false);
        Review review2 = new Review(1, USERID+1, "31.08", 1, 1, "Resena", false);
        Review review3 = new Review(1, USERID+2, "31.08", 1, 1, "Resena", false);
        List<Review> reviews = new ArrayList<>();
        reviews.add(review1);
        reviews.add(review2);
        reviews.add(review3);
        User.UserBuilder userBuilder = new User.UserBuilder(EMAIL, PASSWORD, USERNAME).id(USERID);
        User user = new User(userBuilder);

        Assert.assertTrue(reviewService.didUserReview(reviews, user));

    }
    @Test
    public void TestDidUserReviewFalse(){
        Review review1 = new Review(1, USERID-1, "31.08", 1, 1, "Resena", false);
        Review review2 = new Review(1, USERID+1, "31.08", 1, 1, "Resena", false);
        Review review3 = new Review(1, USERID+2, "31.08", 1, 1, "Resena", false);
        List<Review> reviews = new ArrayList<>();
        reviews.add(review1);
        reviews.add(review2);
        reviews.add(review3);
        User.UserBuilder userBuilder = new User.UserBuilder(EMAIL, PASSWORD, USERNAME).id(USERID);
        User user = new User(userBuilder);

        Assert.assertFalse(reviewService.didUserReview(reviews, user));

    }

    @Test
    public void TestGetReviewStatBySubject(){
        Map<String, ReviewStatistic> map = new HashMap<>();
        ReviewStatistic reviewStatistic = new ReviewStatistic(ID1, COUNT, 2, 1, 0, 3,0,0);
        map.put(ID1, reviewStatistic);
        List<String> subjects = new ArrayList<>();
        subjects.add(ID1);
        subjects.add(ID2);
        when(reviewDao.getReviewStatMapBySubjectList(subjects)).thenReturn(map);

        //Test to see if the method in service passes the subjectId correctly and
        // returns an empty ReviewStatistic in case it is missing
        Subject subject = new Subject(ID1, "sistemas","departamento", new HashSet<>(), new HashSet<>(), new HashSet<>(), 3);
        Subject subject2 = new Subject(ID2, "otra","departamento", new HashSet<>(), new HashSet<>(), new HashSet<>(), 3);
        List<Subject> list = new ArrayList<>();
        list.add(subject);
        list.add(subject2);
        Map<String, ReviewStatistic> toRet = reviewService.getReviewStatMapBySubjectList(list);

        Assert.assertEquals(toRet.size(), 2);
        Assert.assertEquals(toRet.get(ID1).getReviewCount(), COUNT);
        Assert.assertEquals(toRet.get(ID2).getReviewCount(), 0);

    }

}
