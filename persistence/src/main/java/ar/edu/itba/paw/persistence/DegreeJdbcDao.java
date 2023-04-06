package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Degree;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DegreeJdbcDao implements DegreeDao {
    @Override
    public Optional<Degree> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public List<Degree> getAll() {
        return null;
    }

    @Override
    public void insert(Degree degree) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Degree degree) {

    }
}
