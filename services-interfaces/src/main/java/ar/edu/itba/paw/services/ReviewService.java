package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewVote;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.ReviewVoteType;
import ar.edu.itba.paw.models.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.models.exceptions.UnauthorizedException;

import java.util.List;
import java.util.Optional;

public interface ReviewService {

    List<Review> get(final User currentUser, final Long userId, final String subjectId, final int page, final String orderBy, final String dir);
    int getTotalPages(final User currentUser, final Long userId, final String subjectId);
    Review create(final String subjectId, final Review.Builder review);

    Optional<Review> findById(final long id);

    ReviewVote voteReview(final long reviewId, final ReviewVoteType vote);

    boolean didUserReview(final Subject subject, final User user);
    boolean canUserEditReview(final User user, final Review review);

    Review update(final Review.Builder review) throws UnauthorizedException;
    void delete(final Review review) throws UnauthorizedException;
    void delete(final long reviewId);
    List<ReviewVote> getVotes(final Long reviewId, final Long userId, final int page);

    void deleteReviewVote(final long reviewId, final long userId);
}
