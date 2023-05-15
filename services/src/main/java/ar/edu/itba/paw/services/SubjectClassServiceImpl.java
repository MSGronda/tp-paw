package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.SubjectClass;
import ar.edu.itba.paw.persistence.SubjectClassDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectClassServiceImpl implements SubjectClassService{

    private final SubjectClassDao subjectClassDao;

    @Autowired
    public SubjectClassServiceImpl(final SubjectClassDao subjectClassDao) {
        this.subjectClassDao = subjectClassDao;
    }

    @Override
    public List<Subject> getAllSubsWithClassThatUserCanDo(final long userId){
        return subjectClassDao.getAllSubsWithClassThatUserCanDo(userId);
    }

    @Override
    public Optional<SubjectClass> findById(final String s) {
        return Optional.empty();
    }

    @Override
    public List<SubjectClass> getAll() {
        return null;
    }

    @Override
    public List<SubjectClass> getBySubIdRaw(final String s) {
        return subjectClassDao.getBySubIdRaw(s);
    }

    @Override
    public List<SubjectClass> getBySubId(final String s) {
        return subjectClassDao.getBySubId(s);
    }
}
