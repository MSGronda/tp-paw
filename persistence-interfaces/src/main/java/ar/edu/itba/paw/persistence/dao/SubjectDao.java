package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.OrderDir;
import ar.edu.itba.paw.models.enums.SubjectFilterField;
import ar.edu.itba.paw.models.enums.SubjectOrderField;
import ar.edu.itba.paw.persistence.exceptions.SubjectIdAlreadyExistsPersistenceException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface SubjectDao {
    Subject create(Subject subject) throws SubjectIdAlreadyExistsPersistenceException;
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

    void addProfessorsToSubject(Subject subject, List<String> professors);
}
