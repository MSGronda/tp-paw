package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.SubjectClass;

import java.util.List;

public interface SubjectClassDao extends RWDao<String, SubjectClass> {
    List<SubjectClass> getBySubId(final String id);
    List<SubjectClass> getBySubIdRaw(final String s);
    List<Subject> getAllSubsWithClassThatUserCanDo(final long userId);
}
