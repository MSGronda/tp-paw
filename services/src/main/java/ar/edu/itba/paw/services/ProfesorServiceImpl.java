package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Profesor;
import ar.edu.itba.paw.persistence.ProfesorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfesorServiceImpl implements ProfesorService {
    private final ProfesorDao profesorDao;

    @Autowired
    public ProfesorServiceImpl(ProfesorDao profesorDao) {
        this.profesorDao = profesorDao;
    }

    @Override
    public Optional<Profesor> findById(Integer integer) {
        return profesorDao.findById(integer);
    }

    @Override
    public List<Profesor> getAll() {
        return profesorDao.getAll();
    }
}
