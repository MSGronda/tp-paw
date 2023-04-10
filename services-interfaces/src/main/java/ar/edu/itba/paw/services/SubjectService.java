package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;

import java.util.List;

public interface SubjectService extends BaseService<Long, Subject> {
    List<Subject> getAllByDegree(Long idDegree);

    Subject create(String name, String depto, List<Long> idCorrelativas, List<Long> idProfesores, List<Long> idCarreras, Integer creditos);
}
