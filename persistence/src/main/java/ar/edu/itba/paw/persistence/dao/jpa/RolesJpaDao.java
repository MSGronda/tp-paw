package ar.edu.itba.paw.persistence.dao.jpa;

import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.persistence.dao.RolesDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class RolesJpaDao implements RolesDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Role> findById(final Long id) {
        return Optional.ofNullable(em.find(Role.class, id));
    }

    @Override
    public List<Role> getAll() {
        return em.createQuery("from Role", Role.class).getResultList();
    }

    @Override
    public Optional<Role> findByName(String name) {
        return em.createQuery("from Role where name = :name", Role.class)
                .setParameter("name", name)
                .getResultList()
                .stream()
                .findFirst();
    }
}
