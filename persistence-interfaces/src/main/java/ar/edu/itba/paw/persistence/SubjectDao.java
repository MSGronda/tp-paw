package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Subject;

import java.util.List;

public interface SubjectDao extends RWDao<Long, Subject> {

    List<Subject> getAllByDegree(Long idCarrera);

    List<Subject> getAll();


    Subject create(String name, String depto, List<Long> idCorrelativas, List<Long> idProfesores, List<Long> idCarreras, int creditos);
}
