package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Carrera;
import ar.edu.itba.paw.persistence.CarreraDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarreraServiceImpl implements CarreraService {
    private final CarreraDao carreraDao;

    @Autowired
    public CarreraServiceImpl(@Qualifier("carreraDaoMock") CarreraDao carreraDao) {
        this.carreraDao = carreraDao;
    }

    @Override
    public Carrera get(Integer id) {
        return carreraDao.get(id);
    }

    @Override
    public List<Carrera> getAll() {
        return carreraDao.getAll();
    }

    @Override
    public void insert(Carrera carrera) {
        carreraDao.insert(carrera);
    }

    @Override
    public void delete(Integer id) {
        carreraDao.delete(id);
    }

    @Override
    public void update(Carrera carrera) {
        carreraDao.update(carrera);
    }
}
