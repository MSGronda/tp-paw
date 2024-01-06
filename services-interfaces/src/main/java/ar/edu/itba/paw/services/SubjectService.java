package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.models.exceptions.UnauthorizedException;
import ar.edu.itba.paw.models.exceptions.SubjectIdAlreadyExistsException;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface SubjectService {
    Subject create(
            final Subject.Builder builder,
            final List<Long> degreeIds,
            final List<Integer> semesters,
            final List<String> requirementIds,
            final List<String> professors
    );
    void addClass(
            final Subject subject,
            final String classCode,
            final List<String> professors,
            final List<Integer> days,
            final List<LocalTime> startTimes,
            final List<LocalTime> endTimes,
            final List<String> locations,
            final List<String> buildings,
            final List<String> modes
    );

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

    List<Subject> getAll(final int page, final String orderBy, final String dir);

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
              final List<Long> degreeIds,
              final List<Integer> semesters,
              final List<String> requirementIds,
              final List<String> professors,
              final List<String> classIds,
              final List<String> classCodes,
              final List<String> classProfessors,
              final List<String> classDays,
              final List<String> classStartTimes,
              final List<String> classEndTimes,
              final List<String> classBuildings,
              final List<String> classRooms,
              final List<String> classModes
    );

    void editSubject(
            final Subject subject,
            final String name,
            final String department,
            final int credits,
            final List<String> requirementIds
    );

    void setClasses(
            final Subject subject,
            final List<String> codes,
            final List<List<String>> professors,
            final List<List<Integer>> days,
            final List<List<LocalTime>> startTimes,
            final List<List<LocalTime>> endTimes,
            final List<List<String>> locations,
            final List<List<String>> buildings,
            final List<List<String>> modes
    );
}
