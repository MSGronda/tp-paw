package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewStatistic;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReviewDao extends RWDao<Long,Review> {
    Review create(final Integer easy, final Integer timeDemanding, final String text, final String subjectId, final long userId, final String userEmail) throws SQLException;

    List<Review> getAllBySubject(String id);


    Optional<ReviewStatistic> getReviewStatBySubject(String idSub);
    List<ReviewStatistic> getReviewStatBySubjectList(List<String> idSubs);
    Map<String, ReviewStatistic> getReviewStatMapBySubjectList(List<String> idSubs);

}
