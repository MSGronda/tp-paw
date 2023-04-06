package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Profesor;

import java.util.List;
import java.util.Optional;

public interface ProfesorDao extends RWDao<Long,Profesor> {
    Profesor create(String name , List<Long> materias);
    Optional<Profesor> findByIdWithoutSubjects(Long id);
}
