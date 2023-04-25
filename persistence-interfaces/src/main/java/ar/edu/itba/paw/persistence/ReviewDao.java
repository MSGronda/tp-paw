package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Review;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ReviewDao extends RWDao<Long,Review> {
    Review create(final Boolean anonymous,final Integer easy, final Integer timeDemanding, final String text, final String subjectId, final long userId ) throws SQLException;

    void voteReview(Long idUser, Long idReview, int vote);
    void updateVoteOnReview(Long idUser, Long idReview, int vote);
    boolean userVotedOnReview(Long idUser, Long idReview);

    List<Review> getAllBySubject(String id);

    List<Review> getAllUserReviewsWithSubjectName(Long userId);

    List<Review> getAllSubjectReviewsWithUsername(String subjectId);
    Optional<Integer> getDifficultyBySubject(String idsub);
    Optional<Integer> getTimeBySubject(String idsub);

//    List<Review> getCompleteReviewsBySubjectId(String idSub);
//    List<Review> getCompleteReviewsByUserId(Long idUser);
}
