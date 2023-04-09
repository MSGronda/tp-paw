package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Review;
import java.sql.SQLException;

public interface ReviewDao extends RWDao<Long,Review> {
    Review create(final String title,final String text,final long matId,final long userId) throws SQLException;
}
