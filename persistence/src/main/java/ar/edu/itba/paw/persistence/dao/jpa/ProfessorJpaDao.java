package ar.edu.itba.paw.persistence.dao.jpa;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.SubjectClass;
import ar.edu.itba.paw.models.SubjectClassTime;
import ar.edu.itba.paw.persistence.dao.ProfessorDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public class ProfessorJpaDao implements ProfessorDao {
    @PersistenceContext
    private EntityManager em;

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

    @Override
    public void create(Professor professor) {
        em.persist(professor);
    }

    @Override
    public void addSubjectToProfessors(final Subject subject, final List<String> professors){
        for(String singleProfessor : professors){
            Optional<Professor> maybeProfessor = getByName(singleProfessor);
            Professor professor;
            if(!maybeProfessor.isPresent()){
                professor = new Professor(singleProfessor);
                professor.getSubjects().add(subject);
                em.persist(professor);
            }else{
                professor = maybeProfessor.get();
                professor.getSubjects().add(subject);
            }
        }
    }

    @Override
    public void updateSubjectToProfessorsAdd(final Subject subject, final List<Professor> professorsToAdd) {
        subject.getProfessors().addAll(professorsToAdd);
    }

    @Override
    public void updateSubjectToProfessorsCreate(final Subject subject, final List<String> professorsToCreate) {
        for(String prof : professorsToCreate) {
            Professor professor = new Professor(prof);
            em.persist(professor);
            subject.getProfessors().add(professor);
        }
    }

    @Override
    public void updateSubjectToProfessorsRemove(final Subject subject, final List<Professor> professorsToRemove) {
        for(Professor prof: professorsToRemove){
            subject.getProfessors().remove(prof);
        }
    }

    @Override
    public void addProfessorsToClasses(final Subject subject, final List<String> classCodes, final List<List<String>> classProfessors) {
        Map<String, SubjectClass> subjectClassMap = subject.getClassesById();
        for( int i = 0; i < classCodes.size() ; i++){
            SubjectClass subjectClass = subjectClassMap.get(classCodes.get(i));
            for( String professor : classProfessors.get(i)){
                Optional<Professor> maybeProfessor = getByName(professor);
                if(maybeProfessor.isPresent()){
                    Professor professorToAdd = maybeProfessor.get();
                    if( !subjectClass.getProfessors().contains(professorToAdd)){
                        subjectClass.getProfessors().add(professorToAdd);
                    }
                }
            }
        }
    }

    @Override
    public void updateProfessorsToClassesAdd(final Map<SubjectClass, List<Professor>> professorsToAdd) {
        for(Map.Entry<SubjectClass, List<Professor>> entry : professorsToAdd.entrySet()){
            entry.getKey().getProfessors().addAll(entry.getValue());
        }
    }

    @Override
    public void updateProfessorsToClassesUpdate(final Map<SubjectClass, List<Professor>> professorsToUpdate) {
        for(Map.Entry<SubjectClass, List<Professor>> entry : professorsToUpdate.entrySet()){
            entry.getKey().getProfessors().addAll(entry.getValue());
        }
    }

    @Override
    public void updateProfessorsToClassesRemove(final Map<SubjectClass, List<Professor>> professorsToRemove) {
        for(Map.Entry<SubjectClass, List<Professor>> entry : professorsToRemove.entrySet()){
            entry.getKey().getProfessors().removeAll(entry.getValue());
        }
    }

}
