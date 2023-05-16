package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Roles;

import java.util.Optional;

public interface RolesDao extends ReadableDao<Long, Roles> {


    Optional<Roles> findByName(final String name);
}
