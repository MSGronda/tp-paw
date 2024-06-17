package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewVote;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.OrderDir;
import ar.edu.itba.paw.models.enums.ReviewOrderField;
import ar.edu.itba.paw.models.enums.ReviewVoteType;
import ar.edu.itba.paw.models.exceptions.*;
import ar.edu.itba.paw.persistence.dao.ReviewDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewServiceImpl.class);
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

            if(subjectId == null){
                return reviewDao.getAllUserReviews(user.get(), page, ReviewOrderField.parse(orderBy), OrderDir.parse(dir));
            }
            else{
                Optional<Subject> subject = subjectService.findById(subjectId);
                if(!subject.isPresent()) throw new SubjectNotFoundException();
                return reviewDao.getReviewFromSubjectAndUser(subject.get(), user.get());
            }
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
    public int getTotalPages(Long userId, String subjetId) {
        if ( userId != null ){
            return getTotalPagesForUserReviews(userService.findById(userId).orElseThrow(UserNotFoundException::new));
        }else {
            return getTotalPagesForSubjectReviews(subjectService.findById(subjetId).orElseThrow(SubjectNotFoundException::new));
        }
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
    public ReviewVote voteReview(final long reviewId, final ReviewVoteType vote) {
        final Review review = findById(reviewId).orElseThrow(ReviewNotFoundException::new);
        return reviewDao.voteReview(authUserService.getCurrentUser(), review, vote);
    }

    @Transactional
    @Override
    public void deleteReviewVote(final long reviewId, final long userId){
        final Review review = findById(reviewId).orElseThrow(ReviewNotFoundException::new);
        final User user = authUserService.getCurrentUser();

        if(userId != user.getId()){
            LOGGER.warn("Unauthorized user with id: {} attempted to delete review vote with id: {}", userId, reviewId);
            throw new UnauthorizedException();
        }

        reviewDao.deleteReviewVote(authUserService.getCurrentUser(), review);
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
    public Review update(final Review.Builder reviewBuilder) {
        final User user = authUserService.getCurrentUser();
        if(!user.isEditor() && !reviewBuilder.getUser().equals(user)){
            LOGGER.warn("Unauthorized user with id: {} attempted to update review with id: {}", user.getId(), reviewBuilder.getId());
            throw new UnauthorizedException();
        }

        return reviewDao.update(reviewBuilder);
    }

    @Transactional
    @Override
    public void delete(final Review review) throws UnauthorizedException {
        final User user = authUserService.getCurrentUser();
        if(!user.isEditor() && !review.getUser().equals(user)){
            LOGGER.warn("Unauthorized user with id: {} attempted to delete review with id: {}", user.getId(), review.getId());
            throw new UnauthorizedException();
        }

        reviewDao.delete(review);
    }

    @Transactional
    @Override
    public void delete(long reviewId) {
        final Review review = findById(reviewId).orElseThrow(ReviewNotFoundException::new);
        delete(review);
    }

    @Override
    public List<ReviewVote> getVotes(final Long reviewId, final Long userId, final int page){
        if(page < 1)
            throw new InvalidPageNumberException();

        final Review review = findById(reviewId).orElseThrow(ReviewNotFoundException::new);

        if(userId == null)
            return reviewDao.getReviewVotes(review, page);

        final User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);

        final Optional<ReviewVote> vote = reviewDao.getUserVote(user, review);

        if(!vote.isPresent() || page > 1)
            return new ArrayList<>();

        final List<ReviewVote> reviewVotes = new ArrayList<>();
        reviewVotes.add(vote.get());

        return reviewVotes;
    }
}
