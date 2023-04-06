package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Profesor;

import java.util.Optional;

public interface ProfesorService extends BaseService<Long, Profesor>{
    Optional<Profesor> findByIdWithoutMaterias(Long id);
}
