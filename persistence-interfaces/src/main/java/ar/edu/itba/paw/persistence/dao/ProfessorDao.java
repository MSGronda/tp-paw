package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;

import java.util.List;
import java.util.Optional;

public interface ProfessorDao {
    List<Professor> getAll();
    Optional<Professor> findById(final long id);
    void create(Professor professor);

    Optional<Professor> getByName(final String name);

    void addSubjectToProfessors(final Subject subject, final List<String> professors);

    void updateSubjectToProfessors(final Subject subject, final List<String> professors);

    void addProfessorsToClasses(final Subject subject, final List<String> classCodes, final List<List<String>> classProfessors);

    void updateProfessorsToClasses(final Subject sub, final List<String> classIdsList, final List<String> classesList, final List<List<String>> classProfessorsList);
}
