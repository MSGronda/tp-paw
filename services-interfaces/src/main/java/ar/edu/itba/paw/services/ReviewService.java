package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.OrderDir;
import ar.edu.itba.paw.models.enums.ReviewOrderField;
import ar.edu.itba.paw.models.enums.ReviewVoteType;
import ar.edu.itba.paw.services.exceptions.NoGrantedPermissionException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ReviewService extends BaseService<Long, Review> {
    Review create(final Review review) throws SQLException;

    List<Review> getAll();

    List<Review> getAllUserReviews(final User user, final int page, final String orderBy, final String dir);
    int getTotalPagesForUserReviews(final User user);

    List<Review> getAllSubjectReviews(final Subject subject, final int page, final String orderBy, final String dir);
    int getTotalPagesForSubjectReviews(final Subject subject);

    void voteReview(final User user, final Review review, final ReviewVoteType vote);

    Boolean didUserReview(final Subject subject, final User user);

    void update(final Review review) throws NoGrantedPermissionException;
    void delete(final Review review) throws NoGrantedPermissionException;
}
