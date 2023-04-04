package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.persistence.ReviewDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewDao reviewDao;

    @Autowired
    public ReviewServiceImpl(@Qualifier("reviewDaoMock") ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }

    @Override
    public Review get(Integer id) {
        return reviewDao.get(id);
    }

    @Override
    public List<Review> getAll() {
        return reviewDao.getAll();
    }

    @Override
    public void insert(Review review) {
        reviewDao.insert(review);
    }

    @Override
    public void delete(Integer id) {
        reviewDao.delete(id);
    }

    @Override
    public void update(Review review) {
        reviewDao.update(review);
    }
}
