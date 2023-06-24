package ar.edu.itba.paw.persistence.dao.jpa;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.SubjectClass;
import ar.edu.itba.paw.persistence.dao.ProfessorDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ProfessorJpaDao implements ProfessorDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Professor> findById(final long id) {
        return Optional.ofNullable(em.find(Professor.class, id));
    }

    @Override
    public List<Professor> getByName(final String name) {

        return em.createQuery("from Professor as p where p.name = :name", Professor.class)
                        .setParameter("name", name)
                        .getResultList()
                        ;
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
    public void addSubjectToProfessors(final Subject subject, final List<String> professors, final boolean rset){
        if(rset) {
            subject.getProfessors().clear();
            em.flush();
        }
        for(String singleProfessor : professors){
            List<Professor> maybeProfessor = getByName(singleProfessor);
            Professor professor;
            if(maybeProfessor.isEmpty()){
                professor = new Professor(singleProfessor);
                professor.getSubjects().add(subject);
                em.persist(professor);
            }else{
                professor = maybeProfessor.get(0);
                professor.getSubjects().add(subject);
            }
        }
    }

    @Override
    public void addProfessorsToClasses(final Subject subject, final List<String> classCodes, final List<List<String>> classProfessors, final boolean rset) {
        Map<String, SubjectClass> subjectClassMap = subject.getClassesById();
        for( int i = 0; i < classCodes.size() ; i++){
            SubjectClass subjectClass = subjectClassMap.get(classCodes.get(i));
            if(rset) {
                subjectClass.getProfessors().clear();
            }
            for( String professor : classProfessors.get(i)){
                List<Professor> maybeProfessor = getByName(professor);
                if(!maybeProfessor.isEmpty()){
                    if( !subjectClass.getProfessors().contains(maybeProfessor.get(0))){
                        subjectClass.getProfessors().add(maybeProfessor.get(0));
                    }
                }
            }
        }
    }

    @Override
    public void addProfessorsToClassesEdit(final Subject subject, final List<String> classCodes, final List<List<String>> classProfessors, final boolean rset) {
        Map<String, SubjectClass> subjectClassMap = subject.getClassesById();
        for( int i = 0; i < classCodes.size() ; i++){
            SubjectClass subjectClass = subjectClassMap.get(classCodes.get(i));
            if(rset) {
                subjectClass.getProfessors().clear();
                em.flush();
            }
            for( String professor : classProfessors.get(i)){
                List<Professor> maybeProfessor = getAll();
                Professor prof = null;
                for(Professor listProf : maybeProfessor){
                    if(listProf.getName().equals(professor)){
                        prof = listProf;
                    }
                }
                if(prof != null){
                    if( !subjectClass.getProfessors().contains(prof)){
                        subjectClass.getProfessors().add(prof);
                    }
                }
            }
        }
    }

}
