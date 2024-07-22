package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.SubjectClass;
import ar.edu.itba.paw.models.exceptions.InvalidPageNumberException;
import ar.edu.itba.paw.models.exceptions.ProfessorAlreadyExistsWithNameException;
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

    @Transactional
    @Override
    public void addSubjectToProfessors(final Subject subject, final List<String> professors){
        professorDao.addSubjectToProfessors(subject, professors);
    }

    @Transactional
    @Override
    public void addProfessorsToClass(final SubjectClass subjectClass, final List<String> classProfessors){
        professorDao.addProfessorsToClass(subjectClass, classProfessors);
    }

    @Override
    @Transactional
    public void replaceSubjectProfessors(final Subject subject, final List<String> professors){
        professorDao.replaceSubjectProfessors(subject, professors);
    }

    @Transactional
    @Override
    public void replaceClassProfessors(final SubjectClass subjectClass, final List<String> professors) {
        professorDao.replaceClassProfessors(subjectClass, professors);
    }

    @Override
    public List<Professor> searchProfessors(final String subjectId, final String classId, final String q, int page){
        if(page < 1 || page > getTotalPagesForSearch(subjectId, classId, q)){
            throw new InvalidPageNumberException();
        }
        return professorDao.searchProfessors(subjectId, classId, q, page);
    }

    @Override
    public int getTotalPagesForSearch(final String subjectId, final String classId, final String q){
        return professorDao.getTotalPagesForSearch(subjectId, classId, q);
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
    public Professor createProfessor(final String name) {
        final Professor newProfessor = new Professor(name);

        if(professorDao.getByName(name).isPresent()){
            throw new ProfessorAlreadyExistsWithNameException();
        }

        return professorDao.create(newProfessor);
    }

}
