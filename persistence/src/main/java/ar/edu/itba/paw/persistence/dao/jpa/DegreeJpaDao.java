package ar.edu.itba.paw.persistence.dao.jpa;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.DegreeSemester;
import ar.edu.itba.paw.models.DegreeSubject;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.persistence.dao.DegreeDao;
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
    public Degree create(final String name) {
        final Degree deg = new Degree(name);
        em.persist(deg);
        return deg;
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

    @Override
    public void addSubjectToDegrees(final Subject subject, final List<Long> degreeIds, final List<Integer> semesters) {
        for (int i = 0; i < degreeIds.size(); i++) {
            Degree degree = em.find(Degree.class, degreeIds.get(i));
            DegreeSubject ds = new DegreeSubject(degree, subject, semesters.get(i));
            if (!degree.getDegreeSubjects().contains(ds)) {
                degree.getDegreeSubjects().add(ds);
            }
        }
    }

    @Override
    public void updateSubjectToDegrees(final Subject subject, final List<Long> degreeIds, final List<Integer> semesters) {
        // itero por la lista de degrees
        for (int i = 0; i < degreeIds.size(); i++) {
            Degree degree = em.find(Degree.class, degreeIds.get(i));

            //si contiene el degree, 2 casos: si es el mismo semetre -> eliminar, sino actualizar
            if (subject.getDegrees().contains(degree)) {
                int semester = semesters.get(i);

                int size = degree.getDegreeSubjects().size();
                //itero por los degreeSubjects para encontrar el indicado
                for( int j = 0; j < size; j++){
                    DegreeSubject ds = degree.getDegreeSubjects().get(j);
                    if(ds.getSubject().equals(subject)){
                        //semester se cambio, actualizar
                        if(ds.getSemester() != semester){
                            ds.setSemester(semester);
                        }else{
                            //semester es el mismo, eliminar
                            degree.getDegreeSubjects().remove(ds);
                            size--;
                            em.remove(ds);
                        }
                    }
                }
            } else {
                //si no tiene degree, agregar nuevo degreeSubject
                DegreeSubject ds = new DegreeSubject(degree, subject, semesters.get(i));
                em.persist(ds);
                degree.getDegreeSubjects().add(ds);
            }
        }
    }
}

