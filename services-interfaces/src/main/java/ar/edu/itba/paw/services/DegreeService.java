package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.enums.OperationType;

import java.util.*;


public interface DegreeService {
    List<Degree> getAll();
    Optional<Degree> findById(final long id);
    Optional<Degree> findByName(final String name);
    Optional<Degree> findParentDegree(final Subject subject, final User user);
    OptionalInt findSubjectYearForParentDegree(final Subject subject, final User user);
    Degree create(final Degree.Builder builder);
    void addSubjectToDegrees(final Subject subject, final List<Long> degreeIds, final List<Integer> semesters);
    void addSemestersToDegree(final Degree degree, final Map<Integer, List<String>> semesterSubjects);

    void editName(final Degree degree, final String name);
    void editTotalCredits(final Degree degree, final int totalCredits);

    void deleteSemesterFromDegree(final Degree degree, final int semesterId);
    void delete(final Degree degree);

    void editDegreeSemester(final Degree degree, final AbstractMap.SimpleEntry<OperationType, AbstractMap.SimpleEntry<Integer, String>> op);

    void replaceSubjectDegrees(final Subject subject, final List<Long> degreeIds, final List<Integer> semesters);
}
