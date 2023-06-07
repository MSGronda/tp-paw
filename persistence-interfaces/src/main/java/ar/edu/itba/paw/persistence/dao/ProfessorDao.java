package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Professor;

import java.util.List;
import java.util.Map;

public interface ProfessorDao extends ReadableDao<Long, Professor> {
    void create(Professor professor);
}
