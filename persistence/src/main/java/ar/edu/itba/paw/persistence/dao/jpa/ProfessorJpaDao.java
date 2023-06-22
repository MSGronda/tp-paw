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
    public void addSubjectToProfessors(Subject subject, List<String> professors){
        for(String singleProfessor : professors){
            Optional<Professor> maybeProfessor = getByName(singleProfessor);
            Professor professor;
            if( !maybeProfessor.isPresent()) {
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
    public void addProfessorsToClasses(final Subject subject, final List<String> classCodes, final List<List<String>> classProfessors) {
        Map<String, SubjectClass> subjectClassMap = subject.getClassesById();
        for( int i = 0; i < classCodes.size() ; i++){
            SubjectClass subjectClass = subjectClassMap.get(classCodes.get(i));
            for( String professor : classProfessors.get(i)){
                Optional<Professor> maybeProfessor = getByName(professor);
                if(maybeProfessor.isPresent()){
                    if( !subjectClass.getProfessors().contains(maybeProfessor.get())){
                        subjectClass.getProfessors().add(maybeProfessor.get());
                    }
                }
            }
        }
    }
}
