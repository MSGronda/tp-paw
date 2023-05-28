package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.SubjectProgress;
import ar.edu.itba.paw.persistence.exceptions.UserEmailAlreadyTakenPersistenceException;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface UserDao extends RWDao<Long,User> {
    User create(final User user) throws UserEmailAlreadyTakenPersistenceException;

    Optional<User> findByEmail(final String email);
    Optional<User> findUnconfirmedByEmail(final String email);
    Optional<User> findByConfirmToken(final String token);

    void deleteSubjectProgress(final User user, final Subject subject);
    void updateSubjectProgress(final User user, final Subject subject, final SubjectProgress progress);

    void changePassword(final User user, final String password);
    void changeUsername(final User user, final String username);

    void addRole(final User user, final Role role);
    void updateRoles(final User user, final Role role);

    void confirmUser(final User user);

    void setLocale(final User user, final Locale locale);

    void updateConfirmToken(final User user, final String token);

    void updateSubjectProgressList(final User user, final List<String> subjectIdList);
}
