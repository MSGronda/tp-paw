package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;

import java.util.List;
import java.util.Optional;

public interface ProfessorService {
    Optional<Professor> findById(final long id);
    List<Professor> getAll();
    void create(Professor professor);

    void addSubjectToProfessors(final Subject subject, final List<String> professors, final boolean rset);

    void addProfessorsToClasses(final Subject subject, final List<String> classCodes, final List<List<String>> classProfessors, final boolean rset);

    void addProfessorsToClassesEdit(final Subject sub, final List<String> classesList, final List<List<String>> classProfessorsList, boolean b);
}
