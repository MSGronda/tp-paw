package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Role;
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
    public Optional<Role> findById(final Long id) {
        return rolesDao.findById(id);
    }

    @Override
    public List<Role> getAll() {
        return rolesDao.getAll();
    }

    @Override
    public Optional<Role> findByName(final String name) {
        return rolesDao.findByName(name);
    }
}
