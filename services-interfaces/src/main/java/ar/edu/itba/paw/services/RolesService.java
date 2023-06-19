package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Role;

import java.util.List;
import java.util.Optional;

public interface RolesService {
    List<Role> getAll();
    Optional<Role> findById(final long id);
    Optional<Role> findByName(final String name);
}
