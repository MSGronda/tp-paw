package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Subject;
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

    List<Subject> search(final String name, final int page);
    List<Subject> search(
            String name,
            int page,
            Map<SubjectFilterField, String> filters,
            SubjectOrderField orderBy, OrderDir dir
    );
    int getTotalPagesForSearch(final String name, final Map<SubjectFilterField, String> filters);
    Map<SubjectFilterField, List<String>> getRelevantFiltersForSearch(final String name, final Map<SubjectFilterField,String> filters);

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

    void updateClassesToSubject(final Subject subject, final List<String> classesIdsList , final List<String> classesCodesList);

    void addSubjectClassTimes(final Subject subject, final List<String> classCodes, final List<LocalTime> startTimes, final List<LocalTime> endTimes, final List<String> buildings, final List<String> modes, final List<Integer> days, final List<String> rooms);

    void updateSubjectClassTimes(final Subject subject, final List<String> classIdsList, final List<String> classCodes, final List<LocalTime> startTimes, final List<LocalTime> endTimes, final List<String> buildings, final List<String> modes, final List<Integer> days, final List<String> rooms);


    void delete(final Subject subject);

    void editCredits(Subject subject, int credits);
}
