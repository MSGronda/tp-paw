package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.SubjectClass;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;


@Repository
public class ProfessorJpaDao implements ProfessorDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Professor create(Professor professor) {
        em.persist(professor);
        return professor;
    }

    private Professor getOrGenerateProfessor(final String profName){
        final Optional<Professor> maybeProfessor = getByName(profName);
        final Professor professor;
        if(!maybeProfessor.isPresent()){
            professor = new Professor(profName);
            em.persist(professor);
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
            }
        }
    }

    @Override
    public void addProfessorsToClass(final SubjectClass subjectClass, final List<String> classProfessors){
        for(String professorName : classProfessors){
            final Professor professor = getOrGenerateProfessor(professorName);

            if( !subjectClass.getProfessors().contains(professor)) {
                subjectClass.getProfessors().add(professor);
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
    }

    @Override
    public void replaceClassProfessors(final SubjectClass subjectClass, final List<String> professors) {
        final List<Professor> newProfessors = new ArrayList<>();
        for(final String profName : professors){
            newProfessors.add(getOrGenerateProfessor(profName));
        }
        subjectClass.setProfessors(newProfessors);
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

    @Override
    public List<Professor> getAll() {
        return em.createQuery("from Professor", Professor.class)
                .getResultList();
    }
}
