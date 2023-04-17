package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.persistence.ProfessorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProfessorServiceImpl implements ProfessorService {
    private final ProfessorDao professorDao;

    @Autowired
    public ProfessorServiceImpl(ProfessorDao professorDao) {
        this.professorDao = professorDao;
    }

    @Override
    public Optional<Professor> findById(Long id) {
        return professorDao.findById(id);
    }

    @Override
    public List<Professor> getAll() {
        return professorDao.getAll();
    }

    @Override
    public List<Professor> getAllBySubject(String idSubject) {
        return professorDao.getAllBySubject(idSubject);
    }

    @Override
    public Map<String, List<Professor>> getAllGroupedBySubjectId() {
        return professorDao.getAllGroupedBySubjectId();
    }
}
