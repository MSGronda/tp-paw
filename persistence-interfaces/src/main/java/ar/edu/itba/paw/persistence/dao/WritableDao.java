package ar.edu.itba.paw.persistence.dao;

public interface WritableDao<I,T> {
    void insert(final T t);
    void delete(final I id);
    void update(final T t);
}
