package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.OrderDir;
import ar.edu.itba.paw.models.enums.ReviewOrderField;
import ar.edu.itba.paw.models.enums.ReviewVoteType;
import ar.edu.itba.paw.models.exceptions.InvalidPageNumberException;
import ar.edu.itba.paw.models.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.models.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.models.exceptions.UnauthorizedException;
import ar.edu.itba.paw.persistence.dao.ReviewDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewDao reviewDao;
    private final AuthUserService authUserService;
    private final SubjectService subjectService;

    @Autowired
    public ReviewServiceImpl(
            final ReviewDao reviewDao,
            final AuthUserService authUserService,
            final SubjectService subjectService
    ) {
        this.reviewDao = reviewDao;
        this.authUserService = authUserService;
        this.subjectService = subjectService;
    }

    @Transactional
    @Override
    public Review create(final String subjectId, final Review.Builder reviewBuilder) {
        final Subject subject = subjectService.findById(subjectId).orElseThrow(SubjectNotFoundException::new);
        return reviewDao.create(
                reviewBuilder
                        .subject(subject)
        );
    }

    @Override
    public Optional<Review> findById(final long id) {
        return reviewDao.findById(id);
    }

    @Override
    public List<Review> getAll() {
        return reviewDao.getAll();
    }

    @Override
    public List<Review> getAllUserReviews(final User user, final int page, final String orderBy, final String dir) {
        if(page > getTotalPagesForUserReviews(user) || page < 1) throw new InvalidPageNumberException();
        return reviewDao.getAllUserReviews(user, page, ReviewOrderField.parse(orderBy), OrderDir.parse(dir));
    }

    @Override
    public int getTotalPagesForUserReviews(final User user){
        return reviewDao.getTotalPagesForUserReviews(user);
    }

    @Override
    public List<Review> getAllSubjectReviews(final Subject subject, final int page, final String orderBy, final String dir){
        if(page < 1 || page > getTotalPagesForSubjectReviews(subject)) throw new InvalidPageNumberException();
        return reviewDao.getAllSubjectReviews(subject, page, ReviewOrderField.parse(orderBy), OrderDir.parse(dir));
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

    @Override
    public Subject manyReviewsGetFirstSubject(String subjectIds) {
        final String[] array = subjectIds.split(" ");
        return subjectService.findById(array[0]).orElseThrow(SubjectNotFoundException::new);
    }

    @Override
    @Transactional
    public void manyReviewsSubmit(final String subjectIds, final Review.Builder reviewBuilder) {
        final Subject subject = manyReviewsGetFirstSubject(subjectIds);

        if(didUserReview(subject, authUserService.getCurrentUser())) return;

        create(
                subject.getId(),
                reviewBuilder
                    .subject(subject)
        );
    }

    @Override
    public String manyReviewsNextUrl(final String subjectIds, final int current, final int total){
        // Remove first subject id from list
        final String[] idArray = subjectIds.split(" ");
        final StringBuilder sb = new StringBuilder();
        for(int i=1;  i < idArray.length ; i++){
            sb.append(idArray[i]);
            if(i+1< idArray.length){
                sb.append(" ");
            }
        }
        final String nextSubjectIds = sb.toString();

        if(!nextSubjectIds.isEmpty() && current < total){
            return "/many-reviews?r=" + nextSubjectIds + "&current=" + (current + 1) + "&total=" + total;
        }

        return "/";
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
    public void delete(long reviewId) throws UnauthorizedException, ReviewNotFoundException {
        final Review review = findById(reviewId).orElseThrow(ReviewNotFoundException::new);
        delete(review);
    }
}
