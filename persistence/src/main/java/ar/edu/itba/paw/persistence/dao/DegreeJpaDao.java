package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.DegreeSubject;
import ar.edu.itba.paw.models.Subject;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Repository
public class DegreeJpaDao implements DegreeDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Degree create(final Degree degree) {

        em.persist(degree);
        return degree;
    }

    @Override
    public void addSubjectToDegrees(final Subject subject, final List<Long> degreeIds, final List<Integer> semesters) {
        for (int i = 0; i < degreeIds.size(); i++) {
            final Degree degree = em.find(Degree.class, degreeIds.get(i));
            final DegreeSubject ds = new DegreeSubject(degree, subject, semesters.get(i));
            if (!degree.getDegreeSubjects().contains(ds)) {
                degree.getDegreeSubjects().add(ds);
            }
        }
    }

    @Override
    public void delete(final Degree degree) {
        em.remove(degree);
    }

    @Override
    public void editName(final Degree degree, final String name) {
        degree.setName(name);
    }

    @Override
    public void editTotalCredits(final Degree degree, final int totalCredits) {
        degree.setTotalCredits(totalCredits);
    }

    @Override
    public void replaceSubjectDegrees(final Subject subject, final List<Long> degreeIds, final List<Integer> semesters){
        for(final Degree degree : getAll()){
            degree.getDegreeSubjects().removeIf(ds -> {
                if(ds.getSubject().equals(subject)){
                    for(int i=0; i < degreeIds.size() && i < semesters.size(); i++) {
                        // Solo lo elimino si no va a estar presente cuando reemplace con los nuevos
                        if (degree.getId() == degreeIds.get(i) && ds.getSemester() == semesters.get(i)) {
                            return false;
                        }
                    }
                    em.remove(ds);
                    return true;
                }
                return false;
            });
        }
        for (int i = 0; i < degreeIds.size() && i < semesters.size(); i++) {
            final Degree degree = em.find(Degree.class, degreeIds.get(i));
            final DegreeSubject ds = new DegreeSubject(degree, subject, semesters.get(i));
            if(!degree.getDegreeSubjects().contains(ds)){
                em.persist(ds);
                degree.getDegreeSubjects().add(ds);
            }
        }
    }

    @Override
    public Optional<Degree> findById(final long id) {
        return Optional.ofNullable(em.find(Degree.class, id));
    }

    @Override
    public Optional<Degree> findByName(final String name) {
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
    public OptionalInt findSubjectSemesterForDegree(final Subject subject, final Degree degree) {
        @SuppressWarnings("unchecked") final Optional<Integer> res = em.createNativeQuery("SELECT semester FROM subjectsdegrees WHERE idsub = ? AND iddeg = ?")
                .setParameter(1, subject.getId())
                .setParameter(2, degree.getId())
                .getResultList()
                .stream().findFirst();

        return res.map(OptionalInt::of).orElseGet(OptionalInt::empty);
    }
}

