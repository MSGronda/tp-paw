package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;

import java.util.List;

public interface SubjectService extends BaseService<String, Subject> {
    List<Subject> getAllByDegree(Long idDegree);

    Subject create(String id, String name, String depto, List<String> idCorrelativas, List<Long> idProfesores, List<Long> idCarreras, Integer creditos);
}
