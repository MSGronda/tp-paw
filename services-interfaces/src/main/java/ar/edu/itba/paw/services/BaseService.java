package ar.edu.itba.paw.services;

import java.util.List;
import java.util.Optional;

public interface BaseService<I, T> {
    Optional<T> findById(final I id);
    List<T> getAll();
}
