package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Review;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReviewJdbcDao implements ReviewDao {
    @Override
    public Optional<Review> findById(Integer integer) {
        return Optional.empty();
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
