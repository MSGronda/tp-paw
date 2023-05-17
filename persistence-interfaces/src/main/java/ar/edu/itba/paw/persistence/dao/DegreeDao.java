package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Degree;

import java.sql.SQLException;
import java.util.Optional;

public interface DegreeDao extends RWDao<Long, Degree> {
    Degree create(final String nombre) throws SQLException;
    Optional<Degree> getByName(final String name);
    Optional<Integer> getSubjectSemesterForDegree(final String subId);
}
