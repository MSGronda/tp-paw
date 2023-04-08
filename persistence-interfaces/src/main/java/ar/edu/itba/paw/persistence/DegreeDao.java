package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Degree;

import java.sql.SQLException;

public interface DegreeDao extends RWDao<Long, Degree> {
    Degree create(final String nombre) throws SQLException;
}
