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
    public List<Review> getAllBySubject(String idsub){
        return reviewDao.getAllBySubject(idsub);
    }

    @Override
    public Review create(Integer easy, Boolean timeDemanding, String text,String subjectId,long userId, String userEmail) throws SQLException {
        return reviewDao.create(easy, timeDemanding, text, subjectId, userId, userEmail);
    }
}
