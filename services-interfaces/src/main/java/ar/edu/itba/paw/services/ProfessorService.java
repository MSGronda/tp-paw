package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Professor;

import java.util.List;
import java.util.Optional;

public interface ProfessorService {
    Optional<Professor> findById(final long id);
    List<Professor> getAll();
    void create(Professor professor);
}
