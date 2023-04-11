package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Subject;

import java.util.List;

public interface SubjectDao extends RWDao<String, Subject> {

    List<Subject> getByName(String name);

    List<Subject> getAllByDegree(Long idCarrera);

    List<Subject> getAll();


    Subject create(String id, String name, String depto, List<String> idCorrelativas, List<Long> idProfesores, List<Long> idCarreras, int creditos);
}
