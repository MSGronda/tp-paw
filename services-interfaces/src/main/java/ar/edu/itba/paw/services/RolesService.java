package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Roles;

import java.util.Optional;

public interface RolesService extends BaseService<Long, Roles> {

    Optional<Roles> findByName(String name);

}
