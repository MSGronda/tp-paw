package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Roles;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.exceptions.UserEmailAlreadyTakenPersistenceException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserDao extends RWDao<Long,User> {
    User create(final User.UserBuilder userBuilder) throws UserEmailAlreadyTakenPersistenceException;

    Optional<User> getUserWithEmail(final String email);

    Integer deleteUserProgressForSubject(final Long id, final String idSub);
    Integer updateSubjectProgress(final Long id, final String idSub, final Integer newProgress);
    Optional<Integer> getUserSubjectProgress(final Long id, final String idSub);
    Map<String, Integer> getUserAllSubjectProgress(final Long id);

    void changePassword(final Long userId, final String password);

    void editProfile(final Long userId, final String username);

    List<Roles> getUserRoles(final Long userId);

    Integer addIdToUserRoles(final Long roleId, final Long userId);

    Integer updateUserRoles(final Long roleId, final Long userId);

    Optional<User> findUserByConfirmToken(final String token);

    void confirmUser(final long userId);
}
