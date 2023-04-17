package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Subject;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SubjectDao extends RWDao<String, Subject> {

    List<Subject> getByName(String name);
    List<Subject> getByNameOrderedBy(String name, String ob);
    List<Subject> getByNameFiltered(String name, Map<String, String> filters, String ob);

    List<Subject> getAllByDegree(Long idCarrera);

    List<Subject> getAll();

    Map<String, String> findPrerequisitesName(String id);


    Subject create(String id, String name, String depto, Set<String> idCorrelativas, Set<Long> idProfesores, Set<Long> idCarreras, int creditos);

    Map<Long, List<Subject>> getAllGroupedByDegreeId();

    Map<Long, Map<Integer, List<Subject>>> getAllGroupedByDegIdAndSemester();
}
