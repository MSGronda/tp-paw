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
    List<Review> getAllBySubject(String idsub);

    Optional<ReviewStatistic> getReviewStatBySubject(String idSub);

    List<ReviewStatistic> getReviewStatBySubjectIdList(List<String> idSubs);
    Map<String, ReviewStatistic> getReviewStatMapBySubjectIdList(List<String> idSubs);
    Map<String, ReviewStatistic> getReviewStatMapBySubjectList(List<Subject> subjects);

    Review create(Integer easy, Integer timeDemanding, String text,String subjectId,long userId, String userEmail) throws SQLException;
}
