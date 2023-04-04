package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Materia;
import ar.edu.itba.paw.persistence.MateriaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MateriaServiceImpl implements MateriaService {
    private final MateriaDao materiaDao;

    @Autowired
    public MateriaServiceImpl(@Qualifier("materiaDaoMock") MateriaDao materiaDao) {
        this.materiaDao = materiaDao;
    }

    @Override
    public Materia get(Integer id) {
        return materiaDao.get(id);
    }

    @Override
    public List<Materia> getAll() {
        return materiaDao.getAll();
    }

    @Override
    public void insert(Materia materia) {
        materiaDao.insert(materia);
    }

    @Override
    public void delete(Integer id) {
        materiaDao.delete(id);
    }

    @Override
    public void update(Materia materia) {
        materiaDao.update(materia);
    }
}
