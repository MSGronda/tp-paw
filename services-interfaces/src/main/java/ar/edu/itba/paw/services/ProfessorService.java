package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.SubjectClass;

import java.util.List;
import java.util.Optional;

public interface ProfessorService {
    Optional<Professor> findById(final long id);

    List<Professor> getAll();

    void addSubjectToProfessors(final Subject subject, final List<String> professors);

    void addProfessorsToClass(final SubjectClass subjectClass, final List<String> classProfessors);

    void replaceSubjectProfessors(final Subject subject, final List<String> professors);

    void replaceClassProfessors(final SubjectClass subjectClass, final List<String> professors);
}
