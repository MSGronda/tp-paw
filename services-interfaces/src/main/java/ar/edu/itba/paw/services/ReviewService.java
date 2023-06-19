package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.ReviewVoteType;
import ar.edu.itba.paw.models.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.models.exceptions.UnauthorizedException;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    Review create(final String subjectId, final Review review);

    Optional<Review> findById(final long id);

    List<Review> getAll();

    List<Review> getAllUserReviews(final User user, final int page, final String orderBy, final String dir);
    int getTotalPagesForUserReviews(final User user);

    List<Review> getAllSubjectReviews(final Subject subject, final int page, final String orderBy, final String dir);
    int getTotalPagesForSubjectReviews(final Subject subject);

    void voteReview(final long reviewId, final ReviewVoteType vote) throws ReviewNotFoundException;

    boolean didUserReview(final Subject subject, final User user);
    boolean canUserEditReview(final User user, final Review review);

    Subject manyReviewsGetFirstSubject(String subjectIds);
    void manyReviewsSubmit(final String subjectIds, final Review review);
    String manyReviewsNextUrl(final String subjectIds, final int current, final int total);

    void update(final Review review) throws UnauthorizedException;
    void delete(final Review review) throws UnauthorizedException;
    void delete(final long reviewId) throws UnauthorizedException, ReviewNotFoundException;
}
