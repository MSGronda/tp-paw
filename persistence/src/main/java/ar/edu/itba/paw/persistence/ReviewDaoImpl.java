package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Review;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewDaoImpl implements ReviewDao {
    @Override
    public Review get(Integer integer) {
        return null;
    }

    @Override
    public List<Review> getAll() {
        return null;
    }

    @Override
    public void insert(Review review) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Review review) {

    }
}
