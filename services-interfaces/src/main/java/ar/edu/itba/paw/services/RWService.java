package ar.edu.itba.paw.services;

import java.util.List;

public interface RWService<ID, T> {
    T get(ID id);
    List<T> getAll();
    void insert(T t);
    void delete(ID id);
    void update(T t);
}
