package ar.edu.itba.paw.services;

import java.util.List;
import java.util.Optional;

public interface BaseService<ID, T> {
    Optional<T> findById(final ID id);
    List<T> getAll();
}
