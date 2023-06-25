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

    void addProfessorsToClasses(final Subject subject, final List<String> classCodes, final List<List<String>> classProfessors);

    void updateProfessorsToClasses(final Subject sub, final List<String> classIdsList, final List<String> classesList, final List<List<String>> classProfessorsList);

    //functions to create, delete or update professors in subjects
    void updateSubjectToProfessorsAdd(final Subject subject, final List<Professor> professorsToAdd);
    void updateSubjectToProfessorsCreate(final Subject subject, final List<String> professorsToCreate);
    void updateSubjectToProfessorsRemove(final Subject subject, final List<Professor> professorsToRemove);
}
