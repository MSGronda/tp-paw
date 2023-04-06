package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Materia;

import java.util.List;

public interface MateriaDao extends RWDao<Long,Materia> {

    List<Materia> getAllByCarrera(Long idCarrera);

    List<Materia> getAll();


    Materia create(String name, String depto, List<Long> idCorrelativas, List<Long> idProfesores, List<Long> idCarreras, int creditos);
}
