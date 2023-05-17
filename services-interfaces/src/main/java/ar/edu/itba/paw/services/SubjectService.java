package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface SubjectService extends BaseService<String, Subject> {

    Optional<Subject> findById(final String id);
    List<Subject> findByIds(final List<String> ids);

    List<Subject> getAll();
    List<Subject> getAllByDegree(final Long idDegree);
    Map<Long, List<Subject>> getAllGroupedByDegreeId();
    Map<Long, Map<Integer, List<Subject>>> getAllGroupedByDegIdAndSemester();
    Map<Long, Map<Integer, List<Subject>>> getAllGroupedByDegIdAndYear();

    Map<Integer, List<Subject>> getInfSubsByYear(final Long degreeId);

    Map<Long, List<Subject>> getAllElectivesGroupedByDegId();

    List<Subject> getInfElectives(final Long degreeId);

    List<Subject> getByName(final String name);

    List<Subject> getByNameFiltered(final String name, final Map<String,String> filters);

    int getTotalPagesForSubjects(final String name, final Map<String, String> filters);

    List<String> getSubjectsIds(List<Subject> subjects);
    Map<String, Set<String>> getRelevantFilters(final String name, final Map<String,String> filters);
    Map<User,Set<Subject>> getAllUserUnreviewedNotifSubjects();
    void updateUnreviewedNotIfTime();
    int checkPageNum(Map<String,String> params);
    String checkOrder(Map<String,String> params);
    String checkDir(Map<String,String> params);
}
