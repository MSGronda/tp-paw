package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Professor;

import java.util.List;
import java.util.Optional;

public interface ProfessorDao {
    List<Professor> getAll();
    Optional<Professor> findById(final long id);
    void create(Professor professor);
}
