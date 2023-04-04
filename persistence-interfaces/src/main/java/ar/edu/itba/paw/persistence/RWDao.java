package ar.edu.itba.paw.persistence;

import java.util.List;

public interface RWDao<ID,T> {
    T get(ID id);
    List<T> getAll();
    void insert(T t);
    void delete(ID id);
    void update(T t);
}
