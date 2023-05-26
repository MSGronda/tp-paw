package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Subject;

import java.sql.SQLException;
import java.util.Optional;

public interface DegreeDao extends ReadableDao<Long, Degree> {
    Degree create(final String name) throws SQLException;
    Optional<Degree> findByName(final String name);

    Optional<Integer> findSubjectSemesterForDegree(final Subject subject, final Degree degree);
}
