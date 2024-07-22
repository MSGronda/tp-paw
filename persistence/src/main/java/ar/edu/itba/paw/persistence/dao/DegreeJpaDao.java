package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.DegreeSemester;
import ar.edu.itba.paw.models.DegreeSubject;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.exceptions.DegreeNotFoundException;
import ar.edu.itba.paw.models.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.models.utils.DegreeSearchParams;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class DegreeJpaDao implements DegreeDao {
    private final static Logger LOGGER = LoggerFactory.getLogger(DegreeJpaDao.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public Degree create(final Degree degree) {
        em.persist(degree);
        LOGGER.info("Created degree with id: {}", degree.getId());
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
        final long id = degree.getId();
        em.remove(degree);
        LOGGER.info("Deleted degree with id: {}", id);
    }

    @Override
    public Degree editName(final Degree degree, final String name) {
        degree.setName(name);
        LOGGER.info("Changing degree name with id: {}, to {}", degree.getId(), name);
        return degree;
    }

    @Override
    public Degree editTotalCredits(final Degree degree, final int totalCredits) {
        degree.setTotalCredits(totalCredits);
        LOGGER.info("Changing degree credits with id: {}, to {}", degree.getId(), totalCredits);
        return degree;
    }

    @Override
    public List<DegreeSemester> getDegreeSemesterForSubject(final Degree degree, final Subject subject){
        OptionalInt semesterId = findSubjectSemesterForDegree(subject, degree);

        if(!semesterId.isPresent()) return Collections.emptyList();

        ArrayList<DegreeSemester> semester = new ArrayList<>();
        if(semesterId.getAsInt() == Degree.getElectiveId()) {
            semester.add(new DegreeSemester(semesterId.getAsInt(),degree.getElectives()));
        } else {
            semester.add(degree.getSemesters().get(semesterId.getAsInt()));
        }
        return semester;
    }

    public List<DegreeSemester> getDegreeSemesters(final Degree degree){
        List<DegreeSemester> degreeSemesters = degree.getSemesters();
        degreeSemesters.add(new DegreeSemester(Degree.getElectiveId(),degree.getElectives()));
        return degreeSemesters;
    }

    @Override
    public void replaceSubjectDegrees(final Subject subject, final List<Long> degreeIds, final List<Integer> semesters){
        for(final Degree degree : searchDegrees(new DegreeSearchParams(null))){
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
    public List<Degree> searchDegrees(final DegreeSearchParams params) {
        final List<Object> paramValues = new ArrayList<>();
        final StringBuilder queryString = new StringBuilder("SELECT d.id FROM degrees d WHERE ");

        final String finalizedQuery = appendDegreeSearchParams(queryString, paramValues, params);
        
        final Query nativeQuery = createNativeQuery(finalizedQuery, paramValues);


        @SuppressWarnings("unchecked") final List<Long> degreeIds = (List<Long>) nativeQuery
                .getResultList().stream().map(n -> ((Number)n).longValue()).collect(Collectors.toList());;

        if (degreeIds.isEmpty()) return Collections.emptyList();

        TypedQuery<Degree> query = em.createQuery("FROM Degree WHERE id IN (:degreeIds)", Degree.class);

        query.setParameter("degreeIds",degreeIds);

        return query.getResultList();
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

    private String appendDegreeSearchParams(StringBuilder queryString, List<Object> paramValues, DegreeSearchParams params) {

        if(params.hasSubject()) {
           queryString.append("d.id IN (SELECT sd.iddeg FROM subjectsdegrees sd WHERE sd.idsub = ? )");
           paramValues.add(params.getSubjectId());
        }

        return removeExcessSQL(queryString.toString());
    }

    private String removeExcessSQL(String query) {
        String and = " AND ";
        String where = " WHERE ";

        if(query.endsWith(and)){
            return query.substring(0, query.length() - and.length());
        }
        if(query.endsWith(where)){
            return query.substring(0, query.length() - where.length());
        }

        return query;
    }

    private Query createNativeQuery(final String finalizedQuery, final List<Object> paramValues){
        final Query nativeQuery = em.createNativeQuery(finalizedQuery);

        int queryParamNum = 1;
        for(Object paramValue : paramValues){
            nativeQuery.setParameter(queryParamNum++, paramValue);
        }
        return nativeQuery;
    }
}

