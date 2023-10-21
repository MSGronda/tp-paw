package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.models.exceptions.UnauthorizedException;
import ar.edu.itba.paw.models.exceptions.SubjectIdAlreadyExistsException;

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

    List<Subject> get(
            final User user,
            final Long degree,
            final Long semester,
            final Long available,
            final Long unLockable,
            final Long done,
            final Long future,
            final Long plan,
            final String query,
            final Integer credits,
            final String department,
            final Integer difficulty,
            final Integer timeDemand,
            final int page,
            final String orderBy,
            final String dir
    );

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

    int getTotalPagesForSearch(
            final User user,
            final String name,
            final Integer credits,
            final String department,
            final Integer difficulty,
            final Integer time,
            final String orderBy
    );

    Map<String, List<String>> getRelevantFiltersForSearch(
            final User user,
            final String name,
            final Integer credits,
            final String department,
            final Integer difficulty,
            final Integer time,
            final String orderBy
    );

    Map<User,Set<Subject>> getAllUserUnreviewedNotificationSubjects();
    void updateUnreviewedNotificationTime();

    List<Subject> findAllThatUserCanDo(final User user, final int page, final String orderBy, final String dir);
    List<Subject> findAllThatUserHasNotDone(final User user, final int page, final String orderBy, final String dir);
    List<Subject> findAllThatUserHasDone(final User user, final int page, final String orderBy, final String dir);
    List<Subject> findAllThatUserCouldUnlock(final User user, final int page, final String orderBy, final String dir);

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
