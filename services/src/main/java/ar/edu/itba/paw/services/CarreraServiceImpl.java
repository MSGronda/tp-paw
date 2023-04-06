package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Carrera;
import ar.edu.itba.paw.persistence.CarreraDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarreraServiceImpl implements CarreraService {
    private final CarreraDao carreraDao;

    @Autowired
    public CarreraServiceImpl(CarreraDao carreraDao) {
        this.carreraDao = carreraDao;
    }

    @Override
    public Optional<Carrera> findById(Integer id) {
        return carreraDao.findById(id);
    }

    @Override
    public List<Carrera> getAll() {
        return carreraDao.getAll();
    }
}
