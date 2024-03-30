package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.SubjectClass;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProfessorDao {
    List<Professor> getAll();

    Optional<Professor> findById(final long id);

    Professor create(final Professor professor);

    Optional<Professor> getByName(final String name);

    void addSubjectToProfessors(final Subject subject, final List<String> professors);

    void addProfessorsToClass(final SubjectClass subjectClass, final List<String> classProfessors);

    void replaceSubjectProfessors(final Subject subject, final List<String> professors);

    void replaceClassProfessors(final SubjectClass subjectClass, final List<String> professors);
}
