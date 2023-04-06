package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Materia;
import ar.edu.itba.paw.persistence.MateriaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MateriaServiceImpl implements MateriaService {
    private final MateriaDao materiaDao;

    @Autowired
    public MateriaServiceImpl(MateriaDao materiaDao) {
        this.materiaDao = materiaDao;
    }

    @Override
    public Optional<Materia> findById(Long id) {
        return materiaDao.findById(id);
    }

    @Override
    public List<Materia> getAll() {
        return materiaDao.getAll();
    }

    @Override
    public List<Materia> getAllByCarrera(Long idCarrera){
        return materiaDao.getAllByCarrera(idCarrera);
    }

    @Override
    public Materia create(String name, String depto, List<Long> idCorrelativas, List<Long> idProfesores, List<Long> idCarreras, Integer creditos){
        return materiaDao.create(name, depto, idCorrelativas, idProfesores, idCarreras, creditos);
    }
}
