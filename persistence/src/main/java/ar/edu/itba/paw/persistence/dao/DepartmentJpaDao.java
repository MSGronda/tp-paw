package ar.edu.itba.paw.persistence.dao;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class DepartmentJpaDao implements DepartmentDao {
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public List<String> getAll() {
        return em.createQuery(
            "select distinct department from Subject where department is not null and department <> ''",
                String.class
            ).getResultList();
    }
}
