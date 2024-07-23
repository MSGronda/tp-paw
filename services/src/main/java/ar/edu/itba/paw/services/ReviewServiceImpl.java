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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<Review> get(final User currentUser, final Long userId, final String subjectId, final int page, final String orderBy, final String dir){
        if(page < 1 || page > reviewDao.reviewSearchTotalPages(currentUser, subjectId, userId))
            return Collections.emptyList();

        return reviewDao.reviewSearch(currentUser, subjectId, userId, page, ReviewOrderField.parse(orderBy), OrderDir.parse(dir));
    }

    @Override
    public int getTotalPages(final User currentUser, final Long userId, final String subjectId){
        return reviewDao.reviewSearchTotalPages(currentUser, subjectId, userId);
    }


    @Override
    public Optional<Review> findById(final long id) {
        return reviewDao.findById(id);
    }

    @Transactional
    @Override
    public ReviewVote voteReview(final long reviewId, final ReviewVoteType vote) {
        final Review review = findById(reviewId).orElseThrow(ReviewNotFoundException::new);
        return reviewDao.voteReview(authUserService.getCurrentUser(), review, vote);
    }

    @Transactional
    @Override
    public void deleteReviewVote(final Review review, final User user){
        final Optional<ReviewVote> vote = reviewDao.getUserVote(user, review);
        if(!vote.isPresent()){
            LOGGER.warn("User with id: {} attempted to delete a vote in a review with id: {}", user.getId(), review.getId());
            throw new UnauthorizedException();
        }

        reviewDao.deleteReviewVote(authUserService.getCurrentUser(), review);
    }

    @Override
    public boolean didUserReview(final Subject subject, final User user){
        return reviewDao.didUserReview(subject, user);
    }

    @Transactional
    @Override
    public Review update(final Review.Builder reviewBuilder, final User currentUser) {
        if(!reviewBuilder.getUser().equals(currentUser)){
            LOGGER.warn("Unauthorized user with id: {} attempted to update review with id: {}", currentUser.getId(), reviewBuilder.getId());
            throw new UnauthorizedException();
        }

        return reviewDao.update(reviewBuilder);
    }

    @Transactional
    @Override
    public void delete(final User currentUser, final Review review) {
        if(!currentUser.isEditor() && !review.getUser().equals(currentUser)){
            LOGGER.warn("Unauthorized user with id: {} attempted to delete review with id: {}", currentUser.getId(), review.getId());
            throw new UnauthorizedException();
        }

        reviewDao.delete(review);
    }
    @Override
    public List<ReviewVote> getVotes(final Review review, final Long userId, final int page){
        if(page < 1 || (userId == null && page > reviewDao.getTotalVotePages(review)))
            return Collections.emptyList();
        

        if(userId == null){
            return reviewDao.getReviewVotes(review, page);
        }

        final User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);

        final Optional<ReviewVote> vote = reviewDao.getUserVote(user, review);

        if(!vote.isPresent() || page > 1){
            return new ArrayList<>();
        }

        final List<ReviewVote> reviewVotes = new ArrayList<>();
        reviewVotes.add(vote.get());

        return reviewVotes;
    }

    public int getVoteTotalPages(final Review review, final Long userId){
        if(userId == null){
            return reviewDao.getTotalVotePages(review);
        }
        return 1;
    }
}
