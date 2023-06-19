package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Role;

import java.util.List;
import java.util.Optional;

public interface RolesDao {
    List<Role> getAll();

    Optional<Role> findById(final long id);
    Optional<Role> findByName(final String name);
}
