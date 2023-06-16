package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.SubjectProgress;
import ar.edu.itba.paw.models.exceptions.EmailAlreadyTakenException;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public interface UserDao extends RWDao<Long,User> {
    User create(final User user) throws EmailAlreadyTakenException;

    Optional<User> findByEmail(final String email);
    Optional<User> findUnverifiedByEmail(final String email);
    Optional<User> findByConfirmToken(final String token);

    void updateSubjectProgress(final User user, final Subject subject, final SubjectProgress progress);

    void updateUserDegree(final User user, final Degree degree);

    void changePassword(final User user, final String password);
    void changeUsername(final User user, final String username);

    void addRole(final User user, final Role role);

    void confirmUser(final User user);

    void setLocale(final User user, final Locale locale);

    void updateVerificationToken(final User user, final String token);

    void updateSubjectProgressList(final User user, final List<String> subjectIdList, final SubjectProgress progress);

    void addToCurrentSemester(final User user, final SubjectClass subjectClass);
    void removeFromCurrentSemester(final User user, final SubjectClass subjectClass);
    void clearSemester(final User user);
}
