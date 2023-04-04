package ar.edu.itba.paw.persistence.mock;

import ar.edu.itba.paw.models.Profesor;
import ar.edu.itba.paw.persistence.ProfesorDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Repository
public class ProfesorDaoMock implements ProfesorDao {
    private static final List<Profesor> profesores = Arrays.asList(
            new Profesor(0, "Juan Pérez", Collections.singletonList(0)),
            new Profesor(1, "María Gomez", Collections.singletonList(1)),
            new Profesor(2, "Pedro Rodriguez", Collections.singletonList(2)),
            new Profesor(3, "Ana Martinez", Collections.singletonList(3))
    );

    @Override
    public Profesor get(Integer id) {
        return profesores.get(id);
    }

    @Override
    public List<Profesor> getAll() {
        return new ArrayList<>(profesores);
    }

    @Override
    public void insert(Profesor profesor) {
        profesores.add(profesor);
    }

    @Override
    public void delete(Integer id) {
        profesores.remove(id.intValue());
    }

    @Override
    public void update(Profesor profesor) {
        profesores.set(profesor.getId(), profesor);
    }
}
