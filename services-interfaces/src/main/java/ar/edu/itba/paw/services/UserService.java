package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.SubjectProgress;
import ar.edu.itba.paw.models.exceptions.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface UserService {
    Optional<User> getRelevantUser(final Long available, final Long unLockable, final Long done, final Long future, final Long plan);
    User create(final long degreeId, final String completedSubjectIds, final User user, final byte[] profilePic) throws EmailAlreadyTakenException;
    User create(final long degreeId, final String completedSubjectIds, final User user) throws EmailAlreadyTakenException;

    Optional<User> findById(final long id);
    Optional<User> findByEmail(final String email);
    Optional<User> findUnconfirmedByEmail(final String email);

    void resendVerificationEmail(final String email) throws UserNotFoundException;

    void updateSingleSubjectProgress(final User user, final Subject subject, final SubjectProgress progress);
    void updateSingleSubjectProgress(final User user, final String subjectId, final SubjectProgress progress);
    void updateMultipleSubjectProgress(final User user, final List<String> subIds, final SubjectProgress progress);
    void updateMultipleSubjectProgress(final User user, final String subIds, final SubjectProgress progress);

    void changePassword(
            final User user,
            final String newPassword,
            final String oldPasswordInput
    ) throws OldPasswordDoesNotMatchException;

    void editProfile(final User user, final String username);
    void updateProfilePicture(final User user, final byte[] image) throws InvalidImageSizeException;

    boolean isValidRecoveryToken(final String token);
    void recoverPassword(final String token, final String newPassword) throws InvalidTokenException;
    void sendPasswordRecoveryEmail(final String email) throws UserNotFoundException;

    void addRole(final User user, final Role role);
    void makeModerator(final User requesterUser, final long toMakeModeratorId) throws UserNotFoundException, UnauthorizedException;

    void confirmUser(final String token) throws InvalidTokenException;

    void setLocale(final User user, final Locale locale);

    void addToCurrentSemester(final User user, final String subjectId, final String classId)
            throws SubjectNotFoundException, SubjectClassNotFoundException;
    void removeFromCurrentSemester(final User user, final String subjectId, final String classId);
    void clearSemester(final User user);
    String getSemesterSubmitRedirectUrl(final User user);
    void finishSemester(final User user, final String subjectIds);

   void updateUserDegreeAndSubjectProgress(final User user, final Degree degree, final String subjectIds);

   void clearDegree(final User user);
}
