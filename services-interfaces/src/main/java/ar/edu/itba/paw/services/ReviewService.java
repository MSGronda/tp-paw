package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewStatistic;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReviewService extends BaseService<Long, Review> {
    Optional<Review> findById(Long id);

    List<Review> getAll();
    void recalculateStatistics();
    List<Review> getAllUserReviewsWithSubjectName(Long userId);
    List<Review> getAllSubjectReviewsWithUsername(String subjectId, Map<String,String> param);
    List<Review> getAllBySubject(String idsub);

    Optional<ReviewStatistic> getReviewStatBySubject(String idSub);

    Map<String, ReviewStatistic> getReviewStatMapBySubjectList(List<Subject> subjects);
    Map<String, ReviewStatistic> getReviewStatMapBySubjectIdList(List<String> idSubs);
    List<ReviewStatistic> getReviewStatBySubjectIdList(List<String> idSubs);
    Review create(Boolean anonymous,Integer easy, Integer timeDemanding, String text,String subjectId,long userId ) throws SQLException;

    Integer deleteReviewVote(Long idUser, Long idReview);
    Integer voteReview(Long idUser, Long idReview, int vote);
    Map<Long,Integer> userReviewVoteByIdUser(Long idUser);
    Map<Long,Integer> userReviewVoteByIdSubAndIdUser(String idSub, Long idUser);


    Boolean didUserReview(List<Review> reviews, User user);

    Boolean didUserReviewDB(String subjectId, Long userId);

    void update(Review review);

    void updateReviewStatistics( Integer easyBefore, Integer timeDemandingBefore, Review review);

    void delete(Review review);

    void deleteReviewStatistics(Review review);
}
