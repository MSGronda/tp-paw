package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface SubjectDao extends RWDao<String, Subject> {

    List<Subject> getByName(final String name);

    List<Subject> getByNameFiltered(final String name, final Map<String, String> filters);

    int getTotalPagesForSubjects(final String name, final Map<String, String> filters);

    List<Subject> getAllByDegree(final Long idCarrera);

    List<Subject> getAll();

    Optional<Subject> findById(final String id);
    List<Subject> findByIds(final List<String> id);


    Subject create(final String id, String name, final String depto, final Set<String> idCorrelativas,
                   final Set<Long> idProfesores, final Set<Long> idCarreras, final int creditos);

    Map<Long, List<Subject>> getAllGroupedByDegreeId();

    Map<Long, Map<Integer, List<Subject>>> getAllGroupedByDegIdAndSemester();

    Map<Long, Map<Integer, List<Subject>>> getAllGroupedByDegIdAndYear();

    Map<Long, List<Subject>> getAllElectivesGroupedByDegId();

    Map<User,Set<Subject>> getAllUserUnreviewedNotifSubjects();
    void updateUnreviewedNotifTime();
}
