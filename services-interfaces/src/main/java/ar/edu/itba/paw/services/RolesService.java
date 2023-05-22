package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Role;

import java.util.Optional;

public interface RolesService extends BaseService<Long, Role> {

    Optional<Role> findByName(final String name);

}
