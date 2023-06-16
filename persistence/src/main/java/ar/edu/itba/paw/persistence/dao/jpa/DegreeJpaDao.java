package ar.edu.itba.paw.persistence.dao.jpa;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.persistence.dao.DegreeDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class DegreeJpaDao implements DegreeDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Degree create(String name) {
        final Degree deg = new Degree(name);
        em.persist(deg);
        return deg;
    }

    @Override
    public Optional<Degree> findById(Long id) {
        return Optional.ofNullable(em.find(Degree.class, id));
    }

    @Override
    public Optional<Degree> findByName(String name) {
        return em.createQuery("from Degree d where d.name = :name", Degree.class)
                .setParameter("name", name)
                .getResultList()
                .stream().findFirst();
    }

    @Override
    public List<Degree> getAll() {
        return em.createQuery("from Degree", Degree.class)
                .getResultList();
    }

    @Override
    public Optional<Integer> findSubjectSemesterForDegree(final Subject subject, final Degree degree) {
        @SuppressWarnings("unchecked")
        Optional<Integer> res = em.createNativeQuery("SELECT semester FROM subjectsdegrees WHERE idsub = ? AND iddeg = ?")
                .setParameter(1, subject.getId())
                .setParameter(2, degree.getId())
                .getResultList()
                .stream().findFirst();

        return res;
    }
}
