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
    public Optional<Materia> findById(Integer id) {
        return materiaDao.findById(id);
    }

    @Override
    public List<Materia> getAll() {
        return materiaDao.getAll();
    }
}
