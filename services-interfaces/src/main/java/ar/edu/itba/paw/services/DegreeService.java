package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;


public interface DegreeService {
    List<Degree> getAll();
    Optional<Degree> findById(final long id);
    Optional<Degree> findByName(final String name);
    Optional<Degree> findParentDegree(final Subject subject, final User user);
    OptionalInt findSubjectYearForParentDegree(final Subject subject, final User user);
    Map<String, List<String>> getRelevantFiltersForDegree(final Degree degree);

    void create(final Degree.Builder builder);
    void addSubjectToDegrees(final Subject subject, final List<Long> degreeIds, final List<Integer> semesters);
    void updateSubjectToDegrees(final Subject subject, final List<Long> degreeIds, final List<Integer> semesters);
}
