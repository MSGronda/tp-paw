package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.persistence.SubjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class SubjectServiceImpl implements SubjectService {
    private final SubjectDao subjectDao;

    private static final String orderByName = "subname";
    private static final String orderByCredits = "credits";
    private static final String orderById = "id";


    @Autowired
    public SubjectServiceImpl(SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
    }

    @Override
    public Optional<Subject> findById(String id) {
        return subjectDao.findById(id);
    }

    @Override
    public List<Subject> getByName(String name) {
        return subjectDao.getByName(name);
    }

    @Override
    public Map<String, String> findPrerequisitesName(String id){
        return subjectDao.findPrerequisitesName(id);
    }

    @Override
    public List<Subject> getByNameOrderBy(String name, String ob) {
        switch (ob){
            case "credits": return subjectDao.getByNameOrderedBy(name, orderByCredits);
            case "id": return subjectDao.getByNameOrderedBy(name, orderById);
            case "name":default: return subjectDao.getByNameOrderedBy(name, orderByName);
        }
    }

    @Override
    public List<Subject> getByNameFiltered(String name, Map<String,String> filters, String ob) {
        return subjectDao.getByNameFiltered(name, filters, ob);
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
    public Map<Long, List<Subject>> getAllGroupedByDegreeId() {
        return subjectDao.getAllGroupedByDegreeId();
    }

    public Map<Long, Map<Integer, List<Subject>>> getAllGroupedByDegIdAndSemester() {
        return subjectDao.getAllGroupedByDegIdAndSemester();
    }

    @Override
    public Subject create(String id, String name, String depto, Set<String> idCorrelativas, Set<Long> idProfesores, Set<Long> idCarreras, Integer creditos){
        return subjectDao.create(id, name, depto, idCorrelativas, idProfesores, idCarreras, creditos);
    }
}
