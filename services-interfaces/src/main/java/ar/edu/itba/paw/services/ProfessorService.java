package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;

import java.util.List;
import java.util.Optional;

public interface ProfessorService {
    Optional<Professor> findById(final long id);
    List<Professor> getAll();
    void create(Professor professor);

    void addSubjectToProfessors(final Subject subject, final List<String> professors);

    void updateSubjectToProfessors(final Subject subject, final List<String> professors);

    void addProfessorsToClasses(final Subject subject, final List<String> classCodes, final List<List<String>> classProfessors);

    void updateProfessorsToClasses(final Subject subject, final List<String> classIdsList, final List<String> classCodes, final List<List<String>> classProfessors);
}
