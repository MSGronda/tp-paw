package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.persistence.dao.ProfessorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public void create(Professor professor){
        professorDao.create(professor);
    }

    @Transactional
    @Override
    public void addSubjectToProfessors(final Subject subject, final List<String> professors){
        professorDao.addSubjectToProfessors(subject, professors);
    }

    @Transactional
    @Override
    public void updateSubjectToProfessors(final Subject subject, final List<String> professors){
        professorDao.updateSubjectToProfessors(subject, professors);
    }

    @Transactional
    @Override
    public void addProfessorsToClasses(final Subject subject, final List<String> classCodes, final List<List<String>> classProfessors){
        professorDao.addProfessorsToClasses(subject, classCodes, classProfessors);
    }
    @Transactional
    @Override
    public void updateProfessorsToClasses(final Subject sub, final List<String> classIdsList, final List<String> classesList, final List<List<String>> classProfessorsList) {
        professorDao.updateProfessorsToClasses(sub, classIdsList, classesList, classProfessorsList);
    }
}
