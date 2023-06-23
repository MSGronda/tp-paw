package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Subject;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;


public interface DegreeService {
    List<Degree> getAll();
    Optional<Degree> findById(final long id);
    Optional<Degree> findByName(final String name);
    OptionalInt findSubjectYearForDegree(final Subject subject, final Degree degree);
    Map<String, List<String>> getRelevantFiltersForDegree(final Degree degree);
}
