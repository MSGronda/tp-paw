package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewStats;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReviewDao extends RWDao<Long,Review> {
    Review create(final Review review) throws SQLException;

    Integer deleteReviewVoteByReviewId(final Long idReview);
    Integer deleteReviewVote(final Long idUser, final Long idReview);
    Integer voteReview(final Long idUser, final Long idReview, final int vote);
    Integer updateVoteOnReview(final Long idUser, final Long idReview, final int vote);
    boolean userVotedOnReview(final Long idUser, final Long idReview);
    Map<Long,Integer> userReviewVoteByIdSubAndIdUser(final String idSub, final Long idUser);
    Map<Long,Integer> userReviewVoteByIdUser(final Long idUser);

    List<ReviewStats> getReviewStatListByDegreeId(final long id);


    List<Review> getAllBySubject(final String id);

    Map<String, ReviewStats> getReviewStatMapBySubjectList(final List<String> idSubs);
    Optional<ReviewStats> getReviewStatBySubject(final String idSub);
    List<ReviewStats> getReviewStatBySubjectList(final List<String> idSubs);
    List<Review> getAllUserReviewsWithSubjectName(final Long userId,final Map<String, String> params);
    int getTotalPagesFromUserReviews(final Long userId);
    int getTotalPagesForReviews(final String subjectId);
    List<Review> getAllSubjectReviewsWithUsername(final String subjectId, final Map<String,String> params);
    Boolean didUserReviewDB(final String subjectId, final Long userId);

    void update(final Review review);
}
