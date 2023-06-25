package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.SubjectClass;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.OrderDir;
import ar.edu.itba.paw.models.enums.SubjectFilterField;
import ar.edu.itba.paw.models.enums.SubjectOrderField;
import ar.edu.itba.paw.persistence.exceptions.SubjectIdAlreadyExistsPersistenceException;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface SubjectDao {
    Subject create(final Subject subject) throws SubjectIdAlreadyExistsPersistenceException;
    Optional<Subject> findById(final String id);
    List<Subject> getAll();

    List<Subject> search(final User user, final String name, final int page);
    List<Subject> search(
            User user,
            String name,
            int page,
            Map<SubjectFilterField, String> filters,
            SubjectOrderField orderBy, OrderDir dir
    );
    List<Subject> searchAll(
            String name,
            int page,
            Map<SubjectFilterField, String> filters,
            SubjectOrderField orderBy, OrderDir dir
    );
    int getTotalPagesForSearch(final User user, final String name, final Map<SubjectFilterField, String> filters);
    int getTotalPagesForSearchAll(final String name, final Map<SubjectFilterField, String> filters);
    Map<SubjectFilterField, List<String>> getRelevantFiltersForSearch(final String name, final Map<SubjectFilterField,String> filters);
    Map<SubjectFilterField, List<String>> getRelevantFiltersForSearch(final Degree degree, final String name, final Map<SubjectFilterField,String> filters);
    Map<User,Set<Subject>> getAllUserUnreviewedNotificationSubjects();
    void updateUnreviewedNotificationTime();

    List<Subject> findAllThatUserCanDo(User user);
    List<Subject> findAllThatUserHasNotDone(User user);
    List<Subject> findAllThatUserHasDone(User user);
    List<Subject> findAllThatUserCouldUnlock(User user);

    void addPrerequisites(final Subject sub, final List<String> correlativesList);

    void updatePrerequisitesToAdd(final Subject sub, final List<Subject> correlativesList);

    void updatePrerequisitesToRemove(final Subject sub, final List<Subject> correlativesList);

    void addClassesToSubject(final Subject subject, final Set<String> classesSet);

    void updateClassesToSubject(final Subject subject, final List<String> classesCodesToAdd);

    void addSubjectClassTimes(final Subject subject, final List<String> classCodes, final List<LocalTime> startTimes, final List<LocalTime> endTimes, final List<String> buildings, final List<String> modes, final List<Integer> days, final List<String> rooms);

    void createClassLocTime(final SubjectClass subjectClass, final int days, final LocalTime endTimes, final LocalTime startTimes, final String rooms, final String buildings, final String modes);

    void deleteClassLocTime(final long key);

    void updateClassLocTime(final long key, final int days, final String rooms, final String buildings, final String modes, final LocalTime startTimes,final LocalTime endTimes);

    void delete(final Subject subject);

    void editCredits(Subject subject, int credits);
}
