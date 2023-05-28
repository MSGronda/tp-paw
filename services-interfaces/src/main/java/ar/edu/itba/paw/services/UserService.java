package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.SubjectProgress;
import ar.edu.itba.paw.services.exceptions.InvalidImageSizeException;
import ar.edu.itba.paw.services.exceptions.InvalidTokenException;
import ar.edu.itba.paw.services.exceptions.OldPasswordDoesNotMatchException;
import ar.edu.itba.paw.services.exceptions.UserEmailAlreadyTakenException;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public interface UserService extends BaseService<Long, User> {
    User create(final User user, final byte[] profilePic) throws UserEmailAlreadyTakenException;
    User create(final User user) throws UserEmailAlreadyTakenException, IOException;

    Optional<User> findByEmail(final String email);
    Optional<User> findUnconfirmedByEmail(final String email);

    String regenerateConfirmToken(final User user);

    void deleteSubjectProgress(final User user, final Subject subject);
    void updateSubjectProgress(final User user, final Subject subject, final SubjectProgress progress);
    public void updateSubjectProgressWithSubList( final User user, final String subIds);

    void changePassword(
            final User user,
            final String password,
            final String oldPassword,
            final String userOldPassword
    ) throws OldPasswordDoesNotMatchException;

    void editProfile(final User user, final String username);
    void updateProfilePicture(final User user, final byte[] image) throws InvalidImageSizeException;

    String generateRecoveryToken(final String email);

    boolean isValidRecoveryToken(final String token);
    void recoverPassword(final String token, final String newPassword) throws InvalidTokenException;

    void addRole(final User user, final Role role);
    void updateRoles(final User user, final Role role);

    void confirmUser(final String token) throws InvalidTokenException;

    void setLocale(User user, Locale locale);
    void setLocaleAsync(User user, Locale locale);

    Map<Integer, Double> getUserProgressionPerYear(Degree degree, User user);
}
