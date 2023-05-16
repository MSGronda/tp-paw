package ar.edu.itba.paw.persistence.dao;

public interface RWDao<ID,T> extends ReadableDao<ID,T>, WritableDao<ID,T> {

}
