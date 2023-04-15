package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Review;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ReviewService extends BaseService<Long, Review> {
    Optional<Review> findById(Long id);

    List<Review> getAll();

    List<Review> getAllBySubject(String idsub);

    Optional<Integer> getDifficultyBySubject(String idsub);

    Optional<Integer> getTimeBySubject(String idsub);
    Review create(Integer easy, Integer timeDemanding, String text,String subjectId,long userId, String userEmail) throws SQLException;
}
