package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Subject;

import java.util.Optional;


public interface DegreeService extends BaseService<Long, Degree> {
    Optional<Degree> findByName(final String name);
    int findSubjectYearForDegree(final Subject subject, final Degree degree);
}
