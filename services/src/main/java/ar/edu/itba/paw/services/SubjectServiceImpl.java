package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.persistence.SubjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectServiceImpl implements SubjectService {
    private final SubjectDao subjectDao;

    @Autowired
    public SubjectServiceImpl(SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
    }

    @Override
    public Optional<Subject> findById(String id) {
        return subjectDao.findById(id);
    }

    @Override
    public List<Subject> getAll() {
        return subjectDao.getAll();
    }

    @Override
    public List<Subject> getAllByDegree(Long idDegree){
        return subjectDao.getAllByDegree(idDegree);
    }

    @Override
    public Subject create(String id, String name, String depto, List<String> idCorrelativas, List<Long> idProfesores, List<Long> idCarreras, Integer creditos){
        return subjectDao.create(id, name, depto, idCorrelativas, idProfesores, idCarreras, creditos);
    }
}
