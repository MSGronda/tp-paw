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
    public void updateSubjectToProfessors(Subject subject, List<String> professors) {
        //professors de param: tiene solo los cambios
        //si ya existe en subject, eliminarlo
        //si no existe en subject, agregarlo
        for( String prof : professors){
            Optional<Professor> maybeProfessor = getByName(prof);
            if(maybeProfessor.isPresent()){
                Professor professor = maybeProfessor.get();
                Set<Professor> subjectProfessors = subject.getProfessors();
                //lo contiene, eliminarlo
                if(subjectProfessors.contains(professor)){
                    subjectProfessors.remove(professor);
                }else{
                    //no lo contiene, agregarlo
                    subjectProfessors.add(professor);
                }
            }else{
                //no existe, crearlo y agregarlo
                Professor professor = new Professor(prof);
                em.persist(professor);
                subject.getProfessors().add(professor);
//                professor.getSubjects().add(subject);
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
                    Professor professorToAdd = maybeProfessor.get();
                    if( !subjectClass.getProfessors().contains(professorToAdd)){
                        subjectClass.getProfessors().add(professorToAdd);
                    }
                }
            }
        }
    }

    @Override
    public void updateProfessorsToClasses(final Subject subject, final List<String> classIdsList, final List<String> classCodes, final List<List<String>> classProfessors) {
        //Itero por los ids de las clases
        for( int i = 0; i < classIdsList.size() ; i++){
            //si la comsion es nueva, agregar profesores si no existen
            if( classIdsList.get(i).equals("-1")){
                //iterar por profesores, fijarme si ya existen, sino agregar
                for( String professor : classProfessors.get(i)){
                    Optional<Professor> maybeProfessor = getByName(professor);
                    if(maybeProfessor.isPresent()){
                        for( SubjectClass subjectClass : subject.getClasses()){//tengo que meterme en cada clase que me pasaron en frontend
                            //y fijarme que no contenga al profesor. Si no lo contiene lo añado
                            if( subjectClass.getClassId().equals(classCodes.get(i))){
                                Professor professorToAdd = maybeProfessor.get();
                                if( !subjectClass.getProfessors().contains(professorToAdd)){
                                    subjectClass.getProfessors().add(professorToAdd);
                                }
                            }
                        }
                    }
                }
            }else{
                //si class Code no es -1, se updatea comsion
                if( !classCodes.get(i).equals("-1")) {//Ya existe la comision, tengo que modificar un profesor en una comision
                    SubjectClass subjectClassNext = null;
                    //recorremos profesores del parametro
                    //nos fijamos que no esten y se agregan
                    for (String professor : classProfessors.get(i)) {//recorro la lista de profesores
                        Optional<Professor> maybeProfessor = getByName(professor);
                        if (maybeProfessor.isPresent()) {//en teoria siempre deberia existir
                            for (SubjectClass subjectClass : subject.getClasses()) {
                                if (subjectClass.getClassId().equals(classCodes.get(i))) {
                                    subjectClassNext = subjectClass;
                                    Professor professorToAdd = maybeProfessor.get();
                                    if (!subjectClass.getProfessors().contains(professorToAdd)) {
                                        subjectClass.getProfessors().add(professorToAdd);
                                    }
                                }
                            }
                        }
                    }
                    if (subjectClassNext != null) {
                        //iteramos por los profesores de la comsion
                        //si en el param no hay un prof que en la comision si, se borra ese profesor
                        int size = subjectClassNext.getProfessors().size();
                        for( int j = 0 ; j < size ; j++){
                            if( !classProfessors.get(i).contains(subjectClassNext.getProfessors().get(j).getName())){
                                subjectClassNext.getProfessors().remove(j);
                                size--;
                                j--;
                            }
                        }
                    }
                }//else se deberia borrar el profesor solo si es la ultima comision
                //creo que no se puede hacer aca este chequeo
//                }else{
//                    SubjectClassTime subjectClassTime = em.find(SubjectClassTime.class, Long.parseLong(classIdsList.get(i)));
//                    if( subjectClassTime != null){
//                        String classId = subjectClassTime.getSubjectClass().getClassId();
//                        for( SubjectClass subjectClass : subject.getClasses()){
//                            if( subjectClass.getClassId().equals(classId)){
//                                if( subjectClass.getClassTimes().size() == 1) {
//                                    subjectClass.getProfessors().clear();
//                                }
//                                subjectClass.getClassTimes().
//                            }
//                        }
//                    }
//
//                }
            }

        }
    }

}
