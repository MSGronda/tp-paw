package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.DegreeSubject;
import ar.edu.itba.paw.models.Subject;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public interface DegreeDao {
    Degree create(Degree degree);

    List<Degree> getAll();

    Optional<Degree> findById(final long id);

    Optional<Degree> findByName(final String name);

    OptionalInt findSubjectSemesterForDegree(final Subject subject, final Degree degree);

    void addSubjectToDegrees(final Subject subject, List<Long> degreeIds, final List<Integer> semesters);

    Degree editName(final Degree degree, final String name);

    Degree editTotalCredits(final Degree degree, final int totalCredits);

    void delete(Degree degree);

    void replaceSubjectDegrees(final Subject subject, final List<Long> degreeIds, final List<Integer> semesters);
}
