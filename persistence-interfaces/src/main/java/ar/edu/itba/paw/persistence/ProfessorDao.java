package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Professor;

import java.util.List;
import java.util.Optional;

public interface ProfessorDao extends RWDao<Long, Professor> {
    Professor create(String name , List<Long> materias);
    List<Professor> getAllBySubject(String idSubject);
}
