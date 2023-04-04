package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Profesor;
import ar.edu.itba.paw.persistence.ProfesorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfesorServiceImpl implements ProfesorService {
    private final ProfesorDao profesorDao;

    @Autowired
    public ProfesorServiceImpl(@Qualifier("profesorDaoMock") ProfesorDao profesorDao) {
        this.profesorDao = profesorDao;
    }

    @Override
    public Profesor get(Integer integer) {
        return profesorDao.get(integer);
    }

    @Override
    public List<Profesor> getAll() {
        return profesorDao.getAll();
    }

    @Override
    public void insert(Profesor prof) {
        profesorDao.insert(prof);
    }

    @Override
    public void delete(Integer id) {
        profesorDao.delete(id);
    }

    @Override
    public void update(Profesor prof) {
        profesorDao.update(prof);
    }
}
