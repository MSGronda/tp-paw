package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Carrera;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CarreraJdbcDao implements CarreraDao {
    @Override
    public Optional<Carrera> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public List<Carrera> getAll() {
        return null;
    }

    @Override
    public void insert(Carrera carrera) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Carrera carrera) {

    }
}
