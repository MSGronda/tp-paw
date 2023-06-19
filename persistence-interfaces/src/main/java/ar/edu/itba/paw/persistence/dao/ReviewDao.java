package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewVote;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.OrderDir;
import ar.edu.itba.paw.models.enums.ReviewOrderField;
import ar.edu.itba.paw.models.enums.ReviewVoteType;
import java.util.List;
import java.util.Optional;

public interface ReviewDao {
    List<Review> getAll();
    Optional<Review> findById(final long id);

    Review create(final Review review);
    Review update(final Review review);
    void delete(final Review review);

    Optional<ReviewVote> voteReview(final User user, final Review review, final ReviewVoteType vote);
    boolean didUserVote(final User user, final Review review);

    boolean didUserReview(final Subject subject, final User user);

    List<Review> getAllUserReviews(final User user, final int page, final ReviewOrderField orderBy, final OrderDir dir);
    int getTotalPagesForUserReviews(final User user);

    List<Review> getAllSubjectReviews(final Subject subject, final int page, final ReviewOrderField orderBy, final OrderDir dir);
    int getTotalPagesForSubjectReviews(final Subject subject);
}
