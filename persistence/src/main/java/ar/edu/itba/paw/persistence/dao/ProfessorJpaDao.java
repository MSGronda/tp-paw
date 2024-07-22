package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.SubjectClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;


@Repository
public class ProfessorJpaDao implements ProfessorDao {
    private final static Logger LOGGER = LoggerFactory.getLogger(ProfessorJpaDao.class);
    private final static int PAGE_SIZE = 20;
    @PersistenceContext
    private EntityManager em;

    @Override
    public Professor create(final Professor professor) {
        em.persist(professor);
        LOGGER.info("Created professor with id: {}", professor.getId());
        return professor;
    }

    private Professor getOrGenerateProfessor(final String profName){
        final Optional<Professor> maybeProfessor = getByName(profName);
        final Professor professor;
        if(!maybeProfessor.isPresent()){
            professor = new Professor(profName);
            em.persist(professor);
            LOGGER.info("Created professor with id: {}", professor.getId());
        }else{
            professor = maybeProfessor.get();
        }
        return professor;
    }

    @Override
    public void addSubjectToProfessors(final Subject subject, final List<String> professors) {
        for (String professorName : professors) {
            final Professor professor = getOrGenerateProfessor(professorName);

            if(!professor.getSubjects().contains(subject)){
                professor.getSubjects().add(subject);
                LOGGER.info("Added professor with id: {} to subject with id: {}", professor.getId(), subject.getId());
            }
        }
    }

    @Override
    public void addProfessorsToClass(final SubjectClass subjectClass, final List<String> classProfessors){
        for(String professorName : classProfessors){
            final Professor professor = getOrGenerateProfessor(professorName);

            if( !subjectClass.getProfessors().contains(professor)) {
                subjectClass.getProfessors().add(professor);
                LOGGER.info("Added professor with id: {} to subjectClass with id: {}", professor.getId(), subjectClass.getClassId());
            }
        }
    }

    @Override
    public void replaceSubjectProfessors(final Subject subject, final List<String> professors){
        final Set<Professor> newProfessors = new HashSet<>();
        for(final String profName : professors){
            newProfessors.add(getOrGenerateProfessor(profName));
        }
        subject.setProfessors(newProfessors);
        LOGGER.info("Replaced professors of subject with id: {}", subject.getId());
    }

    @Override
    public void replaceClassProfessors(final SubjectClass subjectClass, final List<String> professors) {
        final List<Professor> newProfessors = new ArrayList<>();
        for(final String profName : professors){
            newProfessors.add(getOrGenerateProfessor(profName));
        }
        subjectClass.setProfessors(newProfessors);
        LOGGER.info("Replaced professors of subjectClass with id: {}", subjectClass.getClassId());
    }

    @Override
    public Optional<Professor> findById(final long id) {
        return Optional.ofNullable(em.find(Professor.class, id));
    }

    @Override
    public Optional<Professor> getByName(final String name) {
        return em.createQuery("from Professor as p where p.name = :name", Professor.class)
                .setParameter("name", name)
                .getResultList()
                .stream().findFirst();
    }

    private String sanitizeWildcards(final String s) {
        return s.replaceAll("%", "\\\\%").replaceAll("_", "\\\\_");
    }

    private Query createQuery(final String base, final String subjectId, final String classId, final String q){
        final StringBuilder nativeQuerySb = new StringBuilder(base);
        final List<Object> params = new ArrayList<>();


        if(subjectId != null){
            if(classId == null){
                nativeQuerySb.append(" JOIN professorssubjects ps ON p.id = ps.idprof WHERE ps.idsub = ? ");
                params.add(subjectId);
            }
            else{
                nativeQuerySb.append(" JOIN classprofessors c ON p.id = c.idprof WHERE c.idsub = ? AND c.idclass = ? ");
                params.add(subjectId);
                params.add(classId);
            }
        }
        else if(classId != null){
            nativeQuerySb.append(" JOIN classprofessors c ON p.id = c.idprof WHERE c.idclass = ? ");
            params.add(classId);
        }
        if(q != null){
            if(subjectId != null || classId != null){
                nativeQuerySb.append(" AND p.profname ILIKE ? ");
            }
            else{
                nativeQuerySb.append(" WHERE p.profname ILIKE ? ");
            }
            params.add("%" + sanitizeWildcards(q) + "%");
        }

        final Query nativeQuery = em.createNativeQuery(nativeQuerySb.toString());

        int i=1;
        for(Object param : params){
            nativeQuery.setParameter(i++, param);
        }

        return nativeQuery;
    }

    @Override
    public List<Professor> searchProfessors(final String subjectId, final String classId, final String q, int page) {

        final Query nativeQuery = createQuery("SELECT id FROM professors p ", subjectId, classId, q);

        @SuppressWarnings("unchecked")
        final List<Long> ids = (List<Long>) nativeQuery.setFirstResult((page - 1) * PAGE_SIZE)
                .setMaxResults(PAGE_SIZE)
                .getResultList().stream().map(n -> ((Number)n).longValue()).collect(Collectors.toList());

        if(ids.isEmpty()) return Collections.emptyList();

        return em.createQuery("from Professor where id in :ids", Professor.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public int getTotalPagesForSearch(final String subjectId, final String classId, final String q) {

        final Query nativeQuery = createQuery("SELECT count(*) FROM professors p ", subjectId, classId, q);

        return (int) Math.max(1, Math.ceil(((Number) nativeQuery.getSingleResult()).doubleValue() / PAGE_SIZE));
    }


    @Override
    public List<Professor> getAll() {
        return em.createQuery("from Professor", Professor.class)
                .getResultList();
    }
}
