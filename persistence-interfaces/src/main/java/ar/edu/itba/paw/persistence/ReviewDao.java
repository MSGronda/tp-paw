package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Review;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ReviewDao extends RWDao<Long,Review> {
    Review create(final Integer easy, final Boolean timeDemanding, final String text,final String subjectId,final long userId, final String userEmail) throws SQLException;

    List<Review> getAllBySubject(String id);

    Optional<Integer> getDifficultyBySubject(String idsub);
}
