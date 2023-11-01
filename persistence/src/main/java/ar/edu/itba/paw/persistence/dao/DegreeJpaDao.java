package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.DegreeSubject;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.exceptions.SubjectIdAlreadyExistsException;
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
            final Degree degree = em.find(Degree.class, degreeIds.get(i));
            final DegreeSubject ds = new DegreeSubject(degree, subject, semesters.get(i));
            if (!degree.getDegreeSubjects().contains(ds)) {
                degree.getDegreeSubjects().add(ds);
            }
        }
    }

    @Override
    public void updateInsertSubjectToDegrees(final Subject subject, final List<Degree> degreesToInsert, final List<Integer> semestersToAdd) {
        for( int i = 0 ; i < degreesToInsert.size() ; i++){
            final Degree degree = degreesToInsert.get(i);
            final DegreeSubject ds = new DegreeSubject(degree, subject, semestersToAdd.get(i));
            em.persist(ds);
            degree.getDegreeSubjects().add(ds);
        }
    }

    @Override
    public void updateUpdateSubjectToDegrees(
            final Subject subject,
            final List<DegreeSubject> degreesToUpdate,
            final List<Integer> semestersToUpdate
    ) {
        for( int i = 0 ; i < degreesToUpdate.size() ; i++){
            final DegreeSubject degreeSubject = degreesToUpdate.get(i);
            final Integer semesterToUpdate = semestersToUpdate.get(i);
            degreeSubject.setSemester(semesterToUpdate);
        }
    }

    @Override
    public void updateDeleteSubjectToDegrees(final Subject subject, final List<DegreeSubject> degreesToDelete) {
        //conseguir el DegreeSubject y eliminarlo
        for( DegreeSubject degreeSubject : degreesToDelete){
            degreeSubject.getDegree().getDegreeSubjects().remove(degreeSubject);
            em.remove(degreeSubject);
        }
    }
}

