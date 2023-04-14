package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Review;
import java.sql.SQLException;
import java.util.List;

public interface ReviewDao extends RWDao<Long,Review> {
    Review create(final Boolean easy, final Boolean timeDemanding, final String text,final String subjectId,final long userId) throws SQLException;

    List<Review> getAllBySubject(String id);
}
