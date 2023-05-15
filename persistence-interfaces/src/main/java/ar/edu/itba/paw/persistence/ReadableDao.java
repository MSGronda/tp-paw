package ar.edu.itba.paw.persistence;

import java.util.List;
import java.util.Optional;

public interface ReadableDao<ID,T> {
    Optional<T> findById(final ID id);
    List<T> getAll();
}
