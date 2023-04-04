package ar.edu.itba.paw.persistence.mock;

import ar.edu.itba.paw.models.Carrera;
import ar.edu.itba.paw.persistence.CarreraDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class CarreraDaoMock implements CarreraDao {
    private static final List<Carrera> carreras = Arrays.asList(
            new Carrera(0, "Ingeniería en Informática", Arrays.asList(
                    0, 1, 2, 3, 4
            ))
    );


    @Override
    public Carrera get(Integer id) {
        return carreras.get(id);
    }

    @Override
    public List<Carrera> getAll() {
        return new ArrayList<>(carreras);
    }

    @Override
    public void insert(Carrera carrera) {
        carreras.add(carrera);
    }

    @Override
    public void delete(Integer id) {
        carreras.remove(id.intValue());
    }

    @Override
    public void update(Carrera carrera) {
        carreras.set(carrera.getId(), carrera);
    }
}
