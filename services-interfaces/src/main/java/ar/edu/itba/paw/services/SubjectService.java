package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;

import java.util.List;
import java.util.Map;

public interface SubjectService extends BaseService<String, Subject> {
    List<Subject> getAllByDegree(Long idDegree);
    List<Subject> getByName(String name);
    List<Subject> getByNameOrderBy(String name, String ob);

    List<Subject> getByNameFiltered(String name, Map<String,String> filters, String ob);

    Subject create(String id, String name, String depto, List<String> idCorrelativas, List<Long> idProfesores, List<Long> idCarreras, Integer creditos);
}
