package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.SubjectClass;
import ar.edu.itba.paw.persistence.dao.ProfessorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProfessorServiceImpl implements ProfessorService {
    private final ProfessorDao professorDao;

    @Autowired
    public ProfessorServiceImpl(final ProfessorDao professorDao) {
        this.professorDao = professorDao;
    }

    @Override
    public Optional<Professor> findById(final long id) {
        return professorDao.findById(id);
    }

    @Override
    public List<Professor> getAll() {
        return professorDao.getAll();
    }

    @Transactional
    @Override
    public void addSubjectToProfessors(final Subject subject, final List<String> professors){
        professorDao.addSubjectToProfessors(subject, professors);
    }

    @Transactional
    @Override
    public void updateSubjectToProfessors(final Subject subject, final List<String> professors){

        List<Professor> professorsToAdd = new ArrayList<>();
        List<Professor> professorsToRemove = new ArrayList<>();
        List<String> professorsToCreate = new ArrayList<>();

        //professors de param: tiene solo los cambios
        //si ya existe en subject, eliminarlo
        //si no existe en subject, agregarlo
        for( String prof : professors){
            Optional<Professor> maybeProfessor = professorDao.getByName(prof);
            if(maybeProfessor.isPresent()){
                Professor professor = maybeProfessor.get();
                Set<Professor> subjectProfessors = subject.getProfessors();
                //lo contiene, eliminarlo
                if(subjectProfessors.contains(professor)){
                    professorsToRemove.add(professor);
                }else{
                    //no lo contiene, agregarlo
                    professorsToAdd.add(professor);
                }
            }else{
                //no existe, crearlo y agregarlo
                professorsToCreate.add(prof);
            }
        }
        if(!professorsToAdd.isEmpty()) {
            professorDao.updateSubjectToProfessorsAdd(subject, professorsToAdd);
        }
        if(!professorsToCreate.isEmpty()) {
            professorDao.updateSubjectToProfessorsCreate(subject, professorsToCreate);
        }
        if(!professorsToRemove.isEmpty()) {
            professorDao.updateSubjectToProfessorsRemove(subject, professorsToRemove);
        }

    }

    @Transactional
    @Override
    public void addProfessorsToClasses(final Subject subject, final List<String> classCodes, final List<List<String>> classProfessors){
        professorDao.addProfessorsToClasses(subject, classCodes, classProfessors);
    }
    @Transactional
    @Override
    public void updateProfessorsToClasses(final Subject sub, final List<String> classIdsList, final List<String> classCodes, final List<List<String>> classProfessors) {

        Map<SubjectClass, List<Professor>> professorsToAdd = new HashMap<>();
        Map<SubjectClass, List<Professor>> professorsToUpdate = new HashMap<>();
        Map<SubjectClass, List<Professor>> professorsToRemove = new HashMap<>();

        //Itero por los ids de las clases
        for( int i = 0; i < classIdsList.size() ; i++){
            //si la comsion es nueva, agregar profesores si no existen
            if( Integer.parseInt(classIdsList.get(i)) < 0){
                //iterar por profesores, fijarme si ya existen, sino agregar
                for( String professor : classProfessors.get(i)){
                    Optional<Professor> maybeProfessor = professorDao.getByName(professor);
                    if(maybeProfessor.isPresent()){
                        for( SubjectClass subjectClass : sub.getClasses()){//tengo que meterme en cada clase que me pasaron en frontend
                            //y fijarme que no contenga al profesor. Si no lo contiene lo añado
                            if( subjectClass.getClassId().equals(classCodes.get(i))){
                                Professor professorToAdd = maybeProfessor.get();
                                if( !subjectClass.getProfessors().contains(professorToAdd)){
                                    professorsToAdd.putIfAbsent(subjectClass, new ArrayList<>());
                                    professorsToAdd.get(subjectClass).add(professorToAdd);
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
                        Optional<Professor> maybeProfessor = professorDao.getByName(professor);
                        if (maybeProfessor.isPresent()) {//en teoria siempre deberia existir
                            for (SubjectClass subjectClass : sub.getClasses()) {
                                if (subjectClass.getClassId().equals(classCodes.get(i))) {
                                    subjectClassNext = subjectClass;
                                    Professor professorToAdd = maybeProfessor.get();
                                    if (!subjectClass.getProfessors().contains(professorToAdd)) {
                                        professorsToUpdate.putIfAbsent(subjectClass, new ArrayList<>());
                                        professorsToUpdate.get(subjectClass).add(professorToAdd);
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
                                professorsToRemove.putIfAbsent(subjectClassNext, new ArrayList<>());
                                professorsToRemove.get(subjectClassNext).add(subjectClassNext.getProfessors().get(j));
                            }
                        }
                    }
                }
            }
        }
        professorDao.updateProfessorsToClassesAdd(professorsToAdd);
        professorDao.updateProfessorsToClassesUpdate(professorsToUpdate);
        professorDao.updateProfessorsToClassesRemove(professorsToRemove);
    }
}
