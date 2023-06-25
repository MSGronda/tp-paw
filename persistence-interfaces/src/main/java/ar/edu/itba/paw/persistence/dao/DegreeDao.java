package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.DegreeSubject;
import ar.edu.itba.paw.models.Subject;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public interface DegreeDao {
    Degree create(final String name);

    List<Degree> getAll();

    Optional<Degree> findById(final long id);
    Optional<Degree> findByName(final String name);

    OptionalInt findSubjectSemesterForDegree(final Subject subject, final Degree degree);

    void addSubjectToDegrees(final Subject subject, List<Long> degreeIds, final List<Integer> semesters);

    void updateInsertSubjectToDegrees(final Subject subject, final List<Degree> degreesToInsert, final List<Integer> semestersToAdd);

    void updateUpdateSubjectToDegrees(final Subject subject, final List<DegreeSubject> degreesToUpdate, final List<Integer> semestersToUpdate);

    void updateDeleteSubjectToDegrees(final Subject subject, final List<DegreeSubject> degreesToDelete);
}
