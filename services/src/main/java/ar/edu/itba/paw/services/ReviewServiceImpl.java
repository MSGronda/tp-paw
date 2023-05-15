package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.ReviewDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final List<String> validOrders = Arrays.asList("easy", "timedemanding");

    private static final List<String> validDir = Arrays.asList("asc", "desc");

    private static final Integer VoteUpdate = 1;
    private static final Integer VoteCreated = 2;

    @Autowired
    public ReviewServiceImpl(final ReviewDao reviewDao, final AuthUserService authUserService) {
        this.reviewDao = reviewDao;
        this.authUserService = authUserService;
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
    public List<Review> getAllUserReviewsWithSubjectName(final Long userId) {
        return reviewDao.getAllUserReviewsWithSubjectName(userId);
    }

    @Override
    public int getTotalPagesForReviews(final String subjectId){
        return reviewDao.getTotalPagesForReviews(subjectId);
    }

    @Override
    public List<Review> getAllSubjectReviewsWithUsername(final String subjectId, final Map<String,String> param){
        Map<String, String> validatedParams = new HashMap<>();

        for(Map.Entry<String, String> filter : param.entrySet()){
            if((Objects.equals(filter.getKey(), "order") && validOrders.contains(filter.getValue())) ||
                    (Objects.equals(filter.getKey(), "dir") && validDir.contains(filter.getValue())) ||
                    (Objects.equals(filter.getKey(), "pageNum") && filter.getValue().matches("[0-9]+"))
            ){
                validatedParams.put(filter.getKey(), filter.getValue());
            }
        }

        return reviewDao.getAllSubjectReviewsWithUsername(subjectId,validatedParams);
    }

    @Override
    public List<Review> getAllBySubject(final String idsub){
        return reviewDao.getAllBySubject(idsub);
    }

    @Override
    public Optional<ReviewStatistic> getReviewStatBySubject(final String idSub) {
        return reviewDao.getReviewStatBySubject(idSub);
    }

    public Map<String, ReviewStatistic> getReviewStatMapBySubjectList(final List<Subject> subjects){
        List<String> idSubs = new ArrayList<>();
        for(Subject sub : subjects){
            idSubs.add(sub.getId());
        }

        Map<String, ReviewStatistic> incomplete = reviewDao.getReviewStatMapBySubjectList(idSubs);

        for(String idSub : idSubs){
            incomplete.putIfAbsent(idSub, new ReviewStatistic(idSub));
        }


        return incomplete;
    }

    @Override
    public Map<String, ReviewStatistic> getReviewStatMapBySubjectIdList(final List<String> idSubs){
        return reviewDao.getReviewStatMapBySubjectList(idSubs);
    }

    @Override
    public List<ReviewStatistic> getReviewStatBySubjectIdList(final List<String> idSubs){
        return reviewDao.getReviewStatBySubjectList(idSubs);
    }

    @Transactional
    @Override
    public Review create(final Boolean anonymous, final Integer easy, final Integer timeDemanding, final String text,
                         final String subjectId, final long userId) throws SQLException {
        return reviewDao.create(anonymous,easy, timeDemanding, text, subjectId, userId);
    }
    @Transactional
    @Override
    public Integer deleteReviewVoteByReviewId(final Long idReview) {
        return reviewDao.deleteReviewVoteByReviewId(idReview);
    }


    // vote created: 1, 2 vote updated:
    @Transactional
    @Override
    public Integer voteReview(final Long idUser, final Long idReview, int vote) {

        // only one vote per user on a certain review

        if(reviewDao.userVotedOnReview(idUser,idReview)){
            reviewDao.updateVoteOnReview(idUser, idReview, vote);
            return VoteUpdate;
        }

        reviewDao.voteReview(idUser,idReview,vote);
        return VoteCreated;
    }

    @Override
    public Map<Long,Integer> userReviewVoteByIdUser(final Long idUser){
        return reviewDao.userReviewVoteByIdUser(idUser);
    }
    @Override
    public Map<Long,Integer> userReviewVoteByIdSubAndIdUser(final String idSub, final Long idUser){
        return reviewDao.userReviewVoteByIdSubAndIdUser(idSub,idUser);
    }

    @Override
    public Boolean didUserReview(final List<Review> reviews, final User user){
        if( user == null)
            return false;
        Long userId = user.getId();
        for( Review review : reviews){
            if( userId.equals(review.getUserId()))
                return true;
        }
        return false;
    }

    @Override
    public Boolean didUserReviewDB(final String subjectId, final Long userId){
        return reviewDao.didUserReviewDB(subjectId, userId);
    }

    @Transactional
    @Override
    public void update(final Review review){
        reviewDao.update(review);
    }

    @Transactional
    @Override
    public void delete(final Review review){
        reviewDao.delete(review.getId());
    }

    @Transactional
    @Override
    public void deleteReview(final Review review, final User user, final Boolean isEditor) throws NoGrantedPermissionException {
        if( !checkAuth(review.getUserId(), user.getId(), isEditor))
            throw new NoGrantedPermissionException();

        deleteReviewVoteByReviewId(review.getId());
        delete(review);
    }

    private Boolean checkAuth(final Long reviewUserId, final Long userId, final Boolean isEditor) {
        if( !reviewUserId.equals(userId) && !isEditor )
            return false;
        return true;
    }

    @Transactional
    @Override
    public Integer deleteReviewVote(final Long idUser, final Long idReview){
        return reviewDao.deleteReviewVote(idUser,idReview);
    }

}
