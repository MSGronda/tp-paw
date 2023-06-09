package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Role;

import java.util.Optional;

public interface RolesDao extends ReadableDao<Long, Role> {
    Optional<Role> findByName(final String name);
}
