package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface SubjectService extends BaseService<String, Subject> {

    Optional<Subject> findById(String id);

    Map<String,String> findPrerequisitesName(String id);

    List<Subject> getAll();
    List<Subject> getAllByDegree(Long idDegree);
    Map<Long, List<Subject>> getAllGroupedByDegreeId();
    Map<Long, Map<Integer, List<Subject>>> getAllGroupedByDegIdAndSemester();
    Map<Long, Map<Integer, List<Subject>>> getAllGroupedByDegIdAndYear();

    Map<Integer, List<Subject>> getInfSubsByYear(Long degreeId);

    Map<Long, List<Subject>> getAllElectivesGroupedByDegId();

    List<Subject> getInfElectives(Long degreeId);


    List<Subject> getByName(String name);
    List<Subject> getByNameOrderBy(String name, String ob);

    List<Subject> getByNameFiltered(String name, Map<String,String> filters, String ob);

    Subject create(String id, String name, String depto, Set<String> idCorrelativas, Set<Long> idProfesores, Set<Long> idCarreras, Integer creditos);
}
