package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.OrderDir;
import ar.edu.itba.paw.models.enums.ReviewOrderField;
import ar.edu.itba.paw.models.enums.ReviewVoteType;
import ar.edu.itba.paw.persistence.dao.ReviewDao;
import ar.edu.itba.paw.services.exceptions.NoGrantedPermissionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.*;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewDao reviewDao;
    private final AuthUserService authUserService;

    @Autowired
    public ReviewServiceImpl(final ReviewDao reviewDao, final AuthUserService authUserService) {
        this.reviewDao = reviewDao;
        this.authUserService = authUserService;
    }

    @Transactional
    @Override
    public Review create(final Review review) throws SQLException {
        return reviewDao.create(review);
    }

    @Override
    public Optional<Review> findById(final Long id) {
        return reviewDao.findById(id);
    }

    @Override
    public List<Review> getAll() {
        return reviewDao.getAll();
    }

    @Override
    public List<Review> getAllUserReviews(final User user, final int page, final String orderBy, final String dir) {
        return reviewDao.getAllUserReviews(user, page, ReviewOrderField.parse(orderBy), OrderDir.parse(dir));
    }

    @Override
    public int getTotalPagesForUserReviews(final User user){
        return reviewDao.getTotalPagesForUserReviews(user);
    }

    @Override
    public List<Review> getAllSubjectReviews(final Subject subject, final int page, final String orderBy, final String dir){
        return reviewDao.getAllSubjectReviews(subject, page, ReviewOrderField.parse(orderBy), OrderDir.parse(dir));
    }

    @Override
    public int getTotalPagesForSubjectReviews(final Subject subject){
        return reviewDao.getTotalPagesForSubjectReviews(subject);
    }

    @Transactional
    @Override
    public void voteReview(final User user, final Review review, ReviewVoteType vote) {
        reviewDao.voteReview(user, review, vote);
    }

    @Override
    public Boolean didUserReview(final Subject subject, final User user){
        return reviewDao.didUserReview(subject, user);
    }

    @Transactional
    @Override
    public void update(final Review review) throws NoGrantedPermissionException {
        final User user = authUserService.getCurrentUser();
        if(!user.isEditor() && !review.getUser().equals(user))
            throw new NoGrantedPermissionException();

        reviewDao.update(review);
    }

    @Transactional
    @Override
    public void delete(final Review review) throws NoGrantedPermissionException {
        final User user = authUserService.getCurrentUser();
        if(!user.isEditor() && !review.getUser().equals(user))
            throw new NoGrantedPermissionException();

        reviewDao.delete(review);
    }
}
