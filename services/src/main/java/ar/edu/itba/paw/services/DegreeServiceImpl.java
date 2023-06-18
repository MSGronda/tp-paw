package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.DegreeYear;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.persistence.dao.DegreeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public Optional<Degree> findByName(final String name) {
        return degreeDao.findByName(name);
    }

    @Override
    public OptionalInt findSubjectYearForDegree(final Subject subject, final Degree degree) {
        final OptionalInt semester = degreeDao.findSubjectSemesterForDegree(subject, degree);
        if(!semester.isPresent()) return OptionalInt.empty();

        return OptionalInt.of((int)Math.ceil(semester.getAsInt() / 2.0));
    }

    @Override
    public List<Degree> getAll() {
        return degreeDao.getAll();
    }
}
