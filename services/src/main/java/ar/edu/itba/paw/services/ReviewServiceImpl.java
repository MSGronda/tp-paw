package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.OrderDir;
import ar.edu.itba.paw.models.enums.ReviewOrderField;
import ar.edu.itba.paw.models.enums.ReviewVoteType;
import ar.edu.itba.paw.models.exceptions.*;
import ar.edu.itba.paw.persistence.dao.ReviewDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewDao reviewDao;
    private final AuthUserService authUserService;
    private final SubjectService subjectService;

    private final UserService userService;

    @Autowired
    public ReviewServiceImpl(
            final ReviewDao reviewDao,
            final AuthUserService authUserService,
            final SubjectService subjectService,
            final UserService userService
    ) {
        this.reviewDao = reviewDao;
        this.authUserService = authUserService;
        this.subjectService = subjectService;
        this.userService = userService;
    }

    @Transactional
    @Override
    public Review create(final String subjectId, final Review.Builder reviewBuilder) {
        final Subject subject = subjectService.findById(subjectId).orElseThrow(SubjectNotFoundException::new);

        if(didUserReview(subject, reviewBuilder.getUser())) throw new UserAlreadyReviewedException();

        return reviewDao.create(
                reviewBuilder
                        .subject(subject)
        );
    }

    @Override
    public List<Review> get(final Long userId, final String subjectId, final int page, final String orderBy, final String dir){
        if(userId != null){
            Optional<User> user = userService.findById(userId);

            if(!user.isPresent()) throw new UserNotFoundException();

            if(page > getTotalPagesForUserReviews(user.get()) || page < 1) throw new InvalidPageNumberException();

            return reviewDao.getAllUserReviews(user.get(), page, ReviewOrderField.parse(orderBy), OrderDir.parse(dir));
        }
        else if(subjectId != null){
            Optional<Subject> subject = subjectService.findById(subjectId);

            if(!subject.isPresent()) throw new SubjectNotFoundException();

            if(page < 1 || page > getTotalPagesForSubjectReviews(subject.get())) throw new InvalidPageNumberException();

            return reviewDao.getAllSubjectReviews(subject.get(), page, ReviewOrderField.parse(orderBy), OrderDir.parse(dir));
        }
        return new ArrayList<>();
    }

    @Override
    public Optional<Review> findById(final long id) {
        return reviewDao.findById(id);
    }

    @Override
    public int getTotalPagesForUserReviews(final User user){
        return reviewDao.getTotalPagesForUserReviews(user);
    }


    @Override
    public int getTotalPagesForSubjectReviews(final Subject subject){
        return reviewDao.getTotalPagesForSubjectReviews(subject);
    }

    @Transactional
    @Override
    public void voteReview(final long reviewId, final ReviewVoteType vote) throws ReviewNotFoundException {
        final Review review = findById(reviewId).orElseThrow(ReviewNotFoundException::new);
        reviewDao.voteReview(authUserService.getCurrentUser(), review, vote);
    }

    @Override
    public boolean didUserReview(final Subject subject, final User user){
        return reviewDao.didUserReview(subject, user);
    }

    @Override
    public boolean canUserEditReview(final User user, final Review review) {
        return user.equals(review.getUser());
    }

    @Transactional
    @Override
    public void update(final Review.Builder reviewBuilder) throws UnauthorizedException {
        final User user = authUserService.getCurrentUser();
        if(!user.isEditor() && !reviewBuilder.getUser().equals(user))
            throw new UnauthorizedException();

        reviewDao.update(reviewBuilder);
    }

    @Transactional
    @Override
    public void delete(final Review review) throws UnauthorizedException {
        final User user = authUserService.getCurrentUser();
        if(!user.isEditor() && !review.getUser().equals(user))
            throw new UnauthorizedException();

        reviewDao.delete(review);
    }

    @Transactional
    @Override
    public void delete(long reviewId) {
        final Review review = findById(reviewId).orElseThrow(ReviewNotFoundException::new);
        delete(review);
    }
}
