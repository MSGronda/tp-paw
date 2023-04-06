package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Profesor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProfesorJdbcDao implements ProfesorDao {
    @Override
    public Optional<Profesor> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public List<Profesor> getAll() {
        return null;
    }

    @Override
    public void insert(Profesor profesor) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Profesor profesor) {

    }
}
