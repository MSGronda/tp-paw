package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;

import java.util.List;
import java.util.Optional;

public interface ProfessorDao {
    List<Professor> getAll();
    Optional<Professor> findById(final long id);
    void create(Professor professor);

    List<Professor> getByName(final String name);

    void addSubjectToProfessors(final Subject subject, final List<String> professors, final boolean rset);

    void addProfessorsToClasses(final Subject subject, final List<String> classCodes, final List<List<String>> classProfessors, final boolean rset);

    void addProfessorsToClassesEdit(final Subject sub, final List<String> classesList, final List<List<String>> classProfessorsList, boolean b);
}
