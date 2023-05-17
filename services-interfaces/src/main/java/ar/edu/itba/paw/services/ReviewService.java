package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.exceptions.NoGrantedPermissionException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReviewService extends BaseService<Long, Review> {
    Optional<Review> findById(final Long id);

    List<Review> getAll();
    List<Review> getAllUserReviewsWithSubjectName(final Long userId, final Map<String, String> params);
    int getTotalPagesFromUserReviews(final Long userId);
    int getTotalPagesForReviews(final String subjectId);
    List<Review> getAllSubjectReviewsWithUsername(final String subjectId, final Map<String,String> param);
    List<Review> getAllBySubject(final String idsub);

    Optional<ReviewStats> getReviewStatBySubject(final String idSub);

    Map<String, ReviewStats> getReviewStatMapBySubjectList(final List<Subject> subjects);
    Map<String, ReviewStats> getReviewStatMapBySubjectIdList(final List<String> idSubs);
    List<ReviewStats> getReviewStatBySubjectIdList(final List<String> idSubs);
    Review create(final Review review) throws SQLException;

    Integer deleteReviewVoteByReviewId(final Long idReview);
    Integer updateReviewVote(final Long idUser, final Long idReview, Review.ReviewVote vote);
    Integer deleteReviewVote(final Long idUser, final Long idReview);
    Integer voteReview(final Long idUser, final Long idReview, final int vote);
    Map<Long,Integer> userReviewVoteByIdUser(final Long idUser);
    Map<Long,Integer> userReviewVoteByIdSubAndIdUser(final String idSub, final Long idUser);
    Map<String, ReviewStats> getReviewStatMapByDegreeId(final long id);

    Boolean didUserReview(final List<Review> reviews, final User user);

    Boolean didUserReviewDB(final String subjectId, final Long userId);

    void update(final Review review) throws NoGrantedPermissionException;

    void delete(final Review review);

    void deleteReview(final Review review, final User user, final Boolean isEditor) throws NoGrantedPermissionException;
}
