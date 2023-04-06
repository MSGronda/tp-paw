package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Materia;

import java.util.List;

public interface MateriaService extends BaseService<Long, Materia> {
    List<Materia> getAllByCarrera(Long idCarrera);

    Materia create(String name, String depto, List<Long> idCorrelativas, List<Long> idProfesores, List<Long> idCarreras, Integer creditos);
}
