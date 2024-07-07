package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.OrderDir;
import ar.edu.itba.paw.models.enums.SubjectFilterField;
import ar.edu.itba.paw.models.enums.SubjectOrderField;
import ar.edu.itba.paw.models.utils.SubjectSearchParams;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface SubjectDao {
    Subject create(final Subject subject);
    Optional<Subject> findById(final String id);
    List<Subject> getAll(final int page, final SubjectOrderField orderBy, final OrderDir dir);
    Map<User,Set<Subject>> getAllUserUnreviewedNotificationSubjects();
    void updateUnreviewedNotificationTime();
    List<Subject> superSearch(final SubjectSearchParams params, final int page, final SubjectOrderField orderBy, final OrderDir dir);
    int superSearchTotalPages(final SubjectSearchParams params);
    Map<SubjectFilterField, List<String>> superSearchRelevantFilters(final SubjectSearchParams params);

    void addPrerequisites(final Subject sub, final List<String> correlativesList);

    void delete(final Subject subject);

    SubjectClass addClassToSubject(final Subject subject, final String classCode);

    List<SubjectClassTime> addClassTimesToClass(
            final SubjectClass subjectClass,
            final List<Integer> days,
            final List<LocalTime> startTimes,
            final List<LocalTime> endTimes,
            final List<String> locations,
            final List<String> buildings,
            final List<String> modes
    );

    Subject editSubject(
            final Subject subject,
            final String name,
            final String department,
            final int credits,
            final Set<Subject> prerequisites
    );

    void replaceClassTimes(
            final SubjectClass subjectClass,
            final List<Integer> days,
            final List<LocalTime> startTimes,
            final List<LocalTime> endTimes,
            final List<String> locations,
            final List<String> buildings,
            final List<String> modes
    );

    void removeSubjectClassIfNotPresent(final Subject subject, final List<String> classCodes);

}
