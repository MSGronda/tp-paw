package ar.edu.itba.paw.persistence;

public interface RWDao<ID,T> extends ReadableDao<ID,T>, WritableDao<ID,T> {

}
