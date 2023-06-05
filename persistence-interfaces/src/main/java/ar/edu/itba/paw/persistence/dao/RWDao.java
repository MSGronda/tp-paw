package ar.edu.itba.paw.persistence.dao;

public interface RWDao<I,T> extends ReadableDao<I,T>, WritableDao<I,T> {

}
