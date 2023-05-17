package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.persistence.dao.DegreeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DegreeServiceImpl implements DegreeService {
    private final DegreeDao degreeDao;

    @Autowired
    public DegreeServiceImpl(final DegreeDao degreeDao) {
        this.degreeDao = degreeDao;
    }

    @Override
    public Optional<Degree> findById(final Long id) {
        return degreeDao.findById(id);
    }
    @Override
    public Optional<Degree> getByName(final String name) {
        return degreeDao.getByName(name);
    }

    @Override
    public int getSubjectYearForDegree(final String subId){
        Optional<Integer> semester = degreeDao.getSubjectSemesterForDegree(subId);
        int sem = semester.orElse(-1);
        return (int) Math.ceil(sem / 2.0);
    }

    @Override
    public List<Degree> getAll() {
        return degreeDao.getAll();
    }
}
