package ar.edu.itba.paw.persistence.mock;

import ar.edu.itba.paw.models.Materia;
import ar.edu.itba.paw.persistence.MateriaDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Repository
public class MateriaDaoMock implements MateriaDao {
    public static final List<Materia> materias = Arrays.asList(
            new Materia(0, "Programación Imperativa", "Informática", null, Collections.singletonList(0), Collections.singletonList(0)),
            new Materia(1, "Programación Orientada a Objetos", "Informática", Collections.singletonList(0), Collections.singletonList(1), Collections.singletonList(0)),
            new Materia(2, "Estructuras de Datos y Algoritmos", "Informática", Collections.singletonList(1), Collections.singletonList(2), Collections.singletonList(0)),
            new Materia(3, "Bases de Datos I", "Informática", Collections.singletonList(2), Collections.singletonList(3), Collections.singletonList(0)),
            new Materia(4, "Bases de Datos II", "Informática", Collections.singletonList(3), Collections.singletonList(4), Collections.singletonList(0))
    );

    @Override
    public Materia get(Integer id) {
        return materias.get(id);
    }

    @Override
    public List<Materia> getAll() {
        return new ArrayList<>(materias);
    }

    @Override
    public void insert(Materia materia) {
        materias.add(materia);
    }

    @Override
    public void delete(Integer id) {
        materias.remove(id.intValue());
    }

    @Override
    public void update(Materia materia) {
        materias.set(materia.getId(), materia);
    }
}
