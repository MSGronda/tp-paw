package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Review;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ReviewService extends BaseService<Long, Review> {
    Optional<Review> findById(Long id);

    List<Review> getAll();

    List<Review> getAllBySubject(String idsub);

    Review create(Boolean easy, Boolean timeDemanding, String text,String subjectId,long userId) throws SQLException;
}
