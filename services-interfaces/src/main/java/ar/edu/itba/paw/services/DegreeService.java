package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Degree;

import java.util.Optional;


public interface DegreeService extends BaseService<Long, Degree> {
    Optional<Degree> getByName(final String name);
    int getSubjectYearForDegree(final String subId);
}
