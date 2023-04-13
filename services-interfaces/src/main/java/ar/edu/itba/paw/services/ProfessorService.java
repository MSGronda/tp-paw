package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Professor;

import java.util.List;
import java.util.Optional;

public interface ProfessorService extends BaseService<Long, Professor>{
    List<Professor> getAllBySubject(String idSubject);
}
