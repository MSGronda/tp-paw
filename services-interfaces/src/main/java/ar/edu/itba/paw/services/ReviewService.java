package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewStatistic;
import ar.edu.itba.paw.models.Subject;

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
    void voteReview(Long idUser, Long idReview, int vote);
    Review create(Boolean anonymous,Integer easy, Integer timeDemanding, String text,String subjectId,long userId ) throws SQLException;
}
