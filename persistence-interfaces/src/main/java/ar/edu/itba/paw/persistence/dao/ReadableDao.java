package ar.edu.itba.paw.persistence.dao;

import java.util.List;
import java.util.Optional;

public interface ReadableDao<I,T> {
    Optional<T> findById(final I id);
    List<T> getAll();
}
