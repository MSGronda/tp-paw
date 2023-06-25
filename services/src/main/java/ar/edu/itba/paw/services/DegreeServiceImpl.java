package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.dao.DegreeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DegreeServiceImpl implements DegreeService {
    private final DegreeDao degreeDao;
    private final SubjectService subjectService;

    @Autowired
    public DegreeServiceImpl(
            final DegreeDao degreeDao,
            final SubjectService subjectService
    ) {
        this.degreeDao = degreeDao;
        this.subjectService = subjectService;
    }

    @Override
    public Optional<Degree> findById(final long id) {
        return degreeDao.findById(id);
    }

    @Override
    public Optional<Degree> findByName(final String name) {
        return degreeDao.findByName(name);
    }

    @Override
    public Optional<Degree> findParentDegree(final Subject subject, final User user) {
        final Set<Degree> subjectDegrees = subject.getDegrees();

        if(user != null) {
            final Degree userDegree = user.getDegree();
            if(subjectDegrees.contains(userDegree)) {
                return Optional.of(userDegree);
            }
        }

        return subjectDegrees.stream().findFirst();
    }

    @Override
    public OptionalInt findSubjectYearForParentDegree(final Subject subject, final User user) {
        final Optional<Degree> maybeDegree = findParentDegree(subject, user);
        if(!maybeDegree.isPresent()) return OptionalInt.empty();
        final Degree degree = maybeDegree.get();

        final OptionalInt semester = degreeDao.findSubjectSemesterForDegree(subject, degree);
        if(!semester.isPresent()) return OptionalInt.empty();

        return OptionalInt.of((int)Math.ceil(semester.getAsInt() / 2.0));
    }

    @Override
    public List<Degree> getAll() {
        return degreeDao.getAll();
    }

    @Override
    public Map<String, List<String>> getRelevantFiltersForDegree(Degree degree) {
        return subjectService.getRelevantFiltersForSearch("");
    }
}
