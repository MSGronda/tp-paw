package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewStatistic;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.ReviewDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewDao reviewDao;

    private static final List<String> validOrders = Arrays.asList("easy", "timedemanding");

    private static final List<String> validDir = Arrays.asList("asc", "desc");

    private static final Integer VoteUpdate = 1;
    private static final Integer VoteCreated = 2;

    @Autowired
    public ReviewServiceImpl(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }

    @Override
    public Optional<Review> findById(Long id) {
        return reviewDao.findById(id);
    }

    @Override
    public List<Review> getAll() {
        return reviewDao.getAll();
    }

    @Override
    public List<Review> getAllUserReviewsWithSubjectName(Long userId) {
        return reviewDao.getAllUserReviewsWithSubjectName(userId);
    }

    @Override
    public List<Review> getAllSubjectReviewsWithUsername(String subjectId,Map<String,String> param){
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
    public List<Review> getAllBySubject(String idsub){
        return reviewDao.getAllBySubject(idsub);
    }

    public void recalculateStatistics(){
        reviewDao.recalculateStatistics();
    }

    @Override
    public Optional<ReviewStatistic> getReviewStatBySubject(String idSub) {
        return reviewDao.getReviewStatBySubject(idSub);
    }

    public Map<String, ReviewStatistic> getReviewStatMapBySubjectList(List<Subject> subjects){
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
    public Map<String, ReviewStatistic> getReviewStatMapBySubjectIdList(List<String> idSubs){
        return reviewDao.getReviewStatMapBySubjectList(idSubs);
    }

    @Override
    public List<ReviewStatistic> getReviewStatBySubjectIdList(List<String> idSubs){
        return reviewDao.getReviewStatBySubjectList(idSubs);
    }

    @Override
    public Review create(Boolean anonymous,Integer easy, Integer timeDemanding, String text,String subjectId,long userId) throws SQLException {
        return reviewDao.create(anonymous,easy, timeDemanding, text, subjectId, userId);
    }

    @Override
    public Integer deleteReviewVoteByReviewId(Long idReview) {
        return reviewDao.deleteReviewVoteByReviewId(idReview);
    }


    // vote created: 1, -1 vote updated:
    @Override
    public Integer voteReview(Long idUser, Long idReview, int vote) {

        // only one vote per user on a certain review

        if(reviewDao.userVotedOnReview(idUser,idReview)){
            reviewDao.updateVoteOnReview(idUser, idReview, vote);
            return VoteUpdate;
        }

        reviewDao.voteReview(idUser,idReview,vote);
        return VoteCreated;
    }
    @Override
    public Map<Long,Integer> userReviewVoteByIdUser(Long idUser){
        return reviewDao.userReviewVoteByIdUser(idUser);
    }
    @Override
    public Map<Long,Integer> userReviewVoteByIdSubAndIdUser(String idSub, Long idUser){
        return reviewDao.userReviewVoteByIdSubAndIdUser(idSub,idUser);
    }

    @Override
    public Boolean didUserReview(List<Review> reviews, User user){
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
    public Boolean didUserReviewDB(String subjectId, Long userId){
        return reviewDao.didUserReviewDB(subjectId, userId);
    }

    @Override
    public void update(Review review){
        reviewDao.update(review);
    }

    @Override
    public void updateReviewStatistics( Integer easyBefore, Integer timeDemandingBefore, Review review){
        reviewDao.updateReviewStatistics(easyBefore, timeDemandingBefore, review);
    }

    @Override
    public void delete(Review review){
        reviewDao.delete(review.getId());
    }

    @Override
    public void deleteReviewStatistics(Review review) {
        reviewDao.deleteReviewStatistics(review);
    }

    //    @Override
    @Override
    public Integer deleteReviewVote(Long idUser, Long idReview){
        return reviewDao.deleteReviewVote(idUser,idReview);
    }

//    @Override
//    public List<Review> getCompleteReviewsBySubjectId(String idSub) {
//        return reviewDao.getCompleteReviewsBySubjectId(idSub);
//    }
//
//    @Override
//    public List<Review> getCompleteReviewsByUserId(Long idUser) {
//        return reviewDao.getCompleteReviewsByUserId(idUser);
//    }
}
