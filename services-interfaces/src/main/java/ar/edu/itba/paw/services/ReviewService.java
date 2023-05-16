package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.exceptions.NoGrantedPermissionException;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReviewService extends BaseService<Long, Review> {
    Optional<Review> findById(final Long id);

    List<Review> getAll();
    List<Review> getAllUserReviewsWithSubjectName(final Long userId);
    int getTotalPagesForReviews(final String subjectId);
    List<Review> getAllSubjectReviewsWithUsername(final String subjectId, final Map<String,String> param);
    List<Review> getAllBySubject(final String idsub);

    Optional<ReviewStatistic> getReviewStatBySubject(final String idSub);

    Map<String, ReviewStatistic> getReviewStatMapBySubjectList(final List<Subject> subjects);
    Map<String, ReviewStatistic> getReviewStatMapBySubjectIdList(final List<String> idSubs);
    List<ReviewStatistic> getReviewStatBySubjectIdList(final List<String> idSubs);
    Review create(final Boolean anonymous, final Integer easy, final Integer timeDemanding, final String text,
                  final String subjectId, final long userId ) throws SQLException;

    Integer deleteReviewVoteByReviewId(final Long idReview);
    Integer deleteReviewVote(final Long idUser, final Long idReview);
    Integer voteReview(final Long idUser, final Long idReview, final int vote);
    Map<Long,Integer> userReviewVoteByIdUser(final Long idUser);
    Map<Long,Integer> userReviewVoteByIdSubAndIdUser(final String idSub, final Long idUser);


    Boolean didUserReview(final List<Review> reviews, final User user);

    Boolean didUserReviewDB(final String subjectId, final Long userId);

    void update(final Review review) throws NoGrantedPermissionException;

    void delete(final Review review);

    void deleteReview(final Review review, final User user, final Boolean isEditor) throws NoGrantedPermissionException;
}
