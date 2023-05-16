package ar.edu.itba.paw.persistence.dao;

public interface WritableDao<ID,T> {
    void insert(final T t);
    void delete(final ID id);
    void update(final T t);
}
