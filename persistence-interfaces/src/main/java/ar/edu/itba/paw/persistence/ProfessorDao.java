package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Professor;

import java.util.List;
import java.util.Map;

public interface ProfessorDao extends RWDao<Long, Professor> {
    Professor create(final String name, final List<String> subjects);
    List<Professor> getAllBySubject(final String idSubject);
}
