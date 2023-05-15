package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Professor;

import java.util.List;
import java.util.Map;

public interface ProfessorService extends BaseService<Long, Professor>{
    List<Professor> getAllBySubject(final String idSubject);
    Map<String, List<Professor>> getAllGroupedBySubjectId();
}
