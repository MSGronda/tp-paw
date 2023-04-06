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
    public Optional<Profesor> findById(Long id) {
        return profesorDao.findById(id);
    }

    @Override
    public Optional<Profesor> findByIdWithoutMaterias(Long id){
        return profesorDao.findByIdWithoutSubjects(id);
    }

    @Override
    public List<Profesor> getAll() {
        return profesorDao.getAll();
    }
}
