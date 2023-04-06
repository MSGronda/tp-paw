package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Materia;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MateriaJdbcDao implements MateriaDao {
    @Override
    public Optional<Materia> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public List<Materia> getAll() {
        return null;
    }

    @Override
    public void insert(Materia materia) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Materia materia) {

    }
}
