package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.SubjectFilterField;
import ar.edu.itba.paw.models.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.models.exceptions.UnauthorizedException;
import ar.edu.itba.paw.services.exceptions.SubjectIdAlreadyExistsException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface SubjectService {
    Subject create(final Subject.Builder builder,
                   final String degreeIds,
                   final String semesters,
                   final String requirementIds,
                   final String professors,
                   final String classCodes,
                   final String classProfessors,
                   final String classDays,
                   final String classStartTimes,
                   final String classEndTime,
                   final String classBuildings,
                   final String classRooms,
                   final String classModes) throws SubjectIdAlreadyExistsException;
    List<Subject> getAll();

    Optional<Subject> findById(final String id);

    List<Subject> search(final User user, final String name, final int page);
    List<Subject> search(
            final User user,
            final String name,
            final int page,
            final String orderBy,
            final String dir,
            final Integer credits,
            final String department,
            final Integer difficulty,
            final Integer time
    );
    int getTotalPagesForSearch(final User user, final String name);
    int getTotalPagesForSearch(
            final User user,
            final String name,
            final Integer credits,
            final String department,
            final Integer difficulty,
            final Integer time
    );
    Map<String, List<String>> getRelevantFiltersForSearch(final String name);
    Map<String, List<String>> getRelevantFiltersForSearch(final User user, final String name);
    Map<String, List<String>> getRelevantFiltersForSearch(
            final User user,
            final String name,
            final Integer credits,
            final String department,
            final Integer difficulty,
            final Integer time
    );
    Map<String, List<String>> getRelevantFiltersForSearch(
            final String name,
            final Integer credits,
            final String department,
            final Integer difficulty,
            final Integer time
    );

    Map<User,Set<Subject>> getAllUserUnreviewedNotificationSubjects();
    void updateUnreviewedNotificationTime();

    List<Subject> findAllThatUserCanDo(final User user);
    List<Subject> findAllThatUserHasNotDone(User user);
    List<Subject> findAllThatUserHasDone(User user);
    List<Subject> findAllThatUserCouldUnlock(User user);

    void delete(final User user, final String subjectId) throws UnauthorizedException, SubjectNotFoundException;

    void delete(final User user, final Subject subject) throws UnauthorizedException;


    void edit(
              final String id,
              final int credits,
              final String degreeIds,
              final String semesters,
              final String requirementIds,
              final String professors,
              final String classIds,
              final String classCodes,
              final String classProfessors,
              final String classDays,
              final String classStartTimes,
              final String classEndTime,
              final String classBuildings,
              final String classRooms,
              final String classModes);
}
