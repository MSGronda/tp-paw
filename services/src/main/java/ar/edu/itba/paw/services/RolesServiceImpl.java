package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Roles;
import ar.edu.itba.paw.persistence.dao.RolesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolesServiceImpl implements RolesService{

    private final RolesDao rolesDao;

    @Autowired
    public RolesServiceImpl(final RolesDao rolesDao) {
        this.rolesDao = rolesDao;
    }

    @Override
    public Optional<Roles> findById(final Long id) {
        return rolesDao.findById(id);
    }

    @Override
    public List<Roles> getAll() {
        return rolesDao.getAll();
    }

    @Override
    public Optional<Roles> findByName(final String name) {
        return rolesDao.findByName(name);
    }
}
