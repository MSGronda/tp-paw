package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.SubjectFilterField;
import ar.edu.itba.paw.services.exceptions.SubjectIdAlreadyExistsException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface SubjectService {
    Subject create(final Subject subject) throws SubjectIdAlreadyExistsException;
    List<Subject> getAll();

    Optional<Subject> findById(final String id);

    List<Subject> search(final String name, final int page);
    List<Subject> search(
            final String name,
            final int page,
            final String orderBy,
            final String dir,
            final Integer credits,
            final String department
    );
    int getTotalPagesForSearch(final String name);
    int getTotalPagesForSearch(final String name, final Integer credits, final String department);
    Map<String, List<String>> getRelevantFiltersForSearch(final String name);
    Map<String, List<String>> getRelevantFiltersForSearch(final String name, final Integer credits, final String department);

    Map<User,Set<Subject>> getAllUserUnreviewedNotificationSubjects();
    void updateUnreviewedNotificationTime();

    List<Subject> findAllThatUserCanDo(final User user);
    List<Subject> findAllThatUserHasNotDone(User user);
    List<Subject> findAllThatUserHasDone(User user);
    List<Subject> findAllThatUserCouldUnlock(User user);
}
