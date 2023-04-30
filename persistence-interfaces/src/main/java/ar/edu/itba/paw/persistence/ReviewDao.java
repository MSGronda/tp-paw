package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewStatistic;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReviewDao extends RWDao<Long,Review> {
    Review create(final Boolean anonymous,final Integer easy, final Integer timeDemanding, final String text, final String subjectId, final long userId ) throws SQLException;

    void deleteReviewStatistics(Review review);

    Integer deleteReviewVoteByReviewId(Long idReview);
    Integer deleteReviewVote(Long idUser, Long idReview);
    Integer voteReview(Long idUser, Long idReview, int vote);
    Integer updateVoteOnReview(Long idUser, Long idReview, int vote);
    boolean userVotedOnReview(Long idUser, Long idReview);
    Map<Long,Integer> userReviewVoteByIdSubAndIdUser(String idSub, Long idUser);
    Map<Long,Integer> userReviewVoteByIdUser(Long idUser);


    List<Review> getAllBySubject(String id);

    // and not during normal execution.
    // This method is slow and costly. It is only meant to be used for table migration
    void recalculateStatistics();

    Map<String, ReviewStatistic> getReviewStatMapBySubjectList(List<String> idSubs);
    Optional<ReviewStatistic> getReviewStatBySubject(String idSub);
    List<ReviewStatistic> getReviewStatBySubjectList(List<String> idSubs);
    List<Review> getAllUserReviewsWithSubjectName(Long userId);
    List<Review> getAllSubjectReviewsWithUsername(String subjectId,Map<String,String> params);
    Boolean didUserReviewDB(String subjectId, Long userId);

    void update(Review review);

    void updateReviewStatistics( Integer easyBefore, Integer timeDemandingBefore, Review review);
}
