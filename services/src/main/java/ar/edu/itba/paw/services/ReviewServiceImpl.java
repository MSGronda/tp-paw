package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewStatistic;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.persistence.ReviewDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

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
    public List<Review> getAllBySubject(String idsub){
        return reviewDao.getAllBySubject(idsub);
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
    public Review create(Integer easy, Integer timeDemanding, String text,String subjectId,long userId, String userEmail) throws SQLException {
        return reviewDao.create(easy, timeDemanding, text, subjectId, userId, userEmail);
    }
}
