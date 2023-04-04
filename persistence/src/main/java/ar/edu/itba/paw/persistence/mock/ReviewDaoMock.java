package ar.edu.itba.paw.persistence.mock;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.persistence.ReviewDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class ReviewDaoMock implements ReviewDao {
    private static final List<Review> reviews = Arrays.asList(
            new Review(0, "a@a.a", 0, "review 1"),
            new Review(1, "b@b.b", 1, "review 2"),
            new Review(2, "c@c.c", 2, "review 3")
    );


    @Override
    public Review get(Integer id) {
        return reviews.get(id);
    }

    @Override
    public List<Review> getAll() {
        return new ArrayList<>(reviews);
    }

    @Override
    public void insert(Review review) {
        reviews.add(review);
    }

    @Override
    public void delete(Integer id) {
        reviews.remove(id.intValue());
    }

    @Override
    public void update(Review review) {
        reviews.set(review.getId(), review);
    }
}
