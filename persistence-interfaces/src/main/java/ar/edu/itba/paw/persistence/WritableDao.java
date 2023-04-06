package ar.edu.itba.paw.persistence;

public interface WritableDao<ID,T> {
    void insert(T t);
    void delete(ID id);
    void update(T t);
}
