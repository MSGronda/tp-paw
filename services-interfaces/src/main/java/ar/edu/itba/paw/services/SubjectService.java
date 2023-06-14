package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.SubjectFilterField;
import ar.edu.itba.paw.services.exceptions.SubjectIdAlreadyExistsException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface SubjectService extends BaseService<String, Subject> {

    Subject create(final Subject subject) throws SubjectIdAlreadyExistsException;
    Optional<Subject> findById(final String id);
    List<Subject> findAllThatUserCanDo(final User user);
    List<Subject> findAllThatUserHasNotDone(User user);
    List<Subject> findAllThatUserHasDone(User user);
    List<Subject> findAllThatUserCouldUnlock(User user);
    List<Subject> getAll();

    List<Subject> search(final String name, final int page);
    List<Subject> search(
            final String name,
            final int page,
            final Map<String,String> filters,
            final String orderBy,
            final String dir
    );
    int getTotalPagesForSearch(final String name);
    int getTotalPagesForSearch(final String name, final Map<String, String> filters);
    Map<SubjectFilterField, List<String>> getRelevantFiltersForSearch(final String name);
    Map<SubjectFilterField, List<String>> getRelevantFiltersForSearch(final String name, final Map<String,String> filters);

    Map<User,Set<Subject>> getAllUserUnreviewedNotificationSubjects();
    void updateUnreviewedNotificationTime();
}
