package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.exceptions.InvalidImageSizeException;
import ar.edu.itba.paw.services.exceptions.InvalidTokenException;
import ar.edu.itba.paw.services.exceptions.OldPasswordDoesNotMatchException;
import ar.edu.itba.paw.services.exceptions.UserEmailAlreadyTakenException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public interface UserService extends BaseService<Long, User> {
    User create(final User user, final byte[] profilePic) throws UserEmailAlreadyTakenException;
    User create(final User user) throws UserEmailAlreadyTakenException, IOException;

    String regenerateConfirmToken(long userId);

    Optional<User> getUserWithEmail(final String email);
    Optional<User> getUnconfirmedUserWithEmail(final String email);

    Integer getUserSubjectProgress(final Long id, final String idSub);
    Map<String, Integer> getUserAllSubjectProgress(final Long id);
    Integer deleteUserProgressForSubject(final Long id, final String idSub);
    Integer updateSubjectProgress(final Long id, final String idSub, final User.SubjectProgressEnum newProgress);
    void changePassword(final Long userId, final String password, final String oldPassword,
                        final String userOldPassword) throws OldPasswordDoesNotMatchException;
    void editProfile(final Long userId, final String username);

    void updateProfilePicture(final User user, final byte[] image) throws InvalidImageSizeException;

    String generateRecoveryToken(final String email);

    List<Role> getUserRoles(final Long userId);

    boolean isValidRecoveryToken(final String token);

    void recoverPassword(final String token, final String newPassword) throws InvalidTokenException;

    Integer addIdToUserRoles(final Long roleId, final Long userId);

    Integer updateUserRoles(final Long roleId, final Long userId);

    void confirmUser(final String token) throws InvalidTokenException;

    void setLocale(User user, Locale locale);
    void setLocaleAsync(User user, Locale locale);
}
