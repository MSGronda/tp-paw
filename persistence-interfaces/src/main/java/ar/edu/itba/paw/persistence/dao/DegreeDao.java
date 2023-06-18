package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Subject;

import java.util.Optional;
import java.util.OptionalInt;

public interface DegreeDao extends ReadableDao<Long, Degree> {
    Degree create(final String name);
    Optional<Degree> findByName(final String name);

    OptionalInt findSubjectSemesterForDegree(final Subject subject, final Degree degree);
}
