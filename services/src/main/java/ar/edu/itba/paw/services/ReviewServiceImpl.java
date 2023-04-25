package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.persistence.ReviewDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewDao reviewDao;

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
    public List<Review> getAllBySubject(String idsub){
        return reviewDao.getAllBySubject(idsub);
    }

    @Override
    public Optional<Integer> getDifficultyBySubject(String idsub) {
        return reviewDao.getDifficultyBySubject(idsub);
    }

    @Override
    public Optional<Integer> getTimeBySubject(String idsub) {
        return reviewDao.getTimeBySubject(idsub);
    }

    @Override
    public Review create(Integer easy, Integer timeDemanding, String text,String subjectId,long userId, String userEmail) throws SQLException {
        return reviewDao.create(easy, timeDemanding, text, subjectId, userId, userEmail);
    }

    @Override
    public void voteReview(Long idUser, Long idReview, int vote) {

        // only one vote per user on a certain review

        if(reviewDao.userVotedOnReview(idUser,idReview))
            reviewDao.updateVoteOnReview(idUser, idReview, vote);
        else
            reviewDao.voteReview(idUser,idReview,vote);
    }

    @Override
    public List<Review> getCompleteReviewsBySubjectId(String idSub) {
        return reviewDao.getCompleteReviewsBySubjectId(idSub);
    }

    @Override
    public List<Review> getCompleteReviewsByUserId(Long idUser) {
        return reviewDao.getCompleteReviewsByUserId(idUser);
    }
}
