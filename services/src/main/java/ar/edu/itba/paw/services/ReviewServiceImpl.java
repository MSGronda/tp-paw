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

    @Override
    public String getFirstSubjectIdFromReviewList(String param) {
        final String[] array = param.split(" ");
        return array[0];
    }

    private String removeFirstSubjectIdFromReviewList(String param) {
        final String[] array = param.split(" ");
        final StringBuilder sb = new StringBuilder();
        for(int i=1;  i < array.length ; i++){
            sb.append(array[i]);
            if(i+1< array.length){
                sb.append(" ");
            }
        }
        return sb.toString();
    }
    @Override
    public boolean validReviewParam(final Map<String, String> params){
        return params.containsKey("r") && !params.get("r").equals("") && params.containsKey("total") && params.containsKey("current");
    }

    @Override
    public boolean nextReviewInList(final Map<String, String> params){
        if(!validReviewParam(params)){
            return false;
        }
        params.put("r", removeFirstSubjectIdFromReviewList(params.get("r")));

        params.put("current", Integer.toString(Integer.parseInt(params.get("current")) + 1));

        return validReviewParam(params);
    }
    @Override
    public String regenerateManyReviewParams(final Map<String, String> params){
        return "?r=" + params.getOrDefault("r","") + "&current=" + params.getOrDefault("current", "") + "&total=" + params.getOrDefault("total", "");
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
