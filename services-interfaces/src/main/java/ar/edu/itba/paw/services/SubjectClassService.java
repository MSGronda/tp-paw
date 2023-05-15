package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.SubjectClass;

import java.util.List;
import java.util.Optional;

public interface SubjectClassService extends BaseService<String, SubjectClass>{
    List<SubjectClass> getBySubIdRaw(final String s);
    List<SubjectClass> getBySubId(final String s);
    List<Subject> getAllSubsWithClassThatUserCanDo(final long userId);
}
