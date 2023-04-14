package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectService extends BaseService<String, Subject> {

    Optional<Subject> findById(String id);

    List<Subject> getAll();
    List<Subject> getAllByDegree(Long idDegree);

    Subject create(String id, String name, String depto, List<String> idCorrelativas, List<Long> idProfesores, List<Long> idCarreras, Integer creditos);
}
