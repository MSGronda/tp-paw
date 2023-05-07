package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Roles;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.exceptions.InvalidTokenException;
import ar.edu.itba.paw.services.exceptions.OldPasswordDoesNotMatchException;
import ar.edu.itba.paw.services.exceptions.UserEmailAlreadyTakenException;
import ar.edu.itba.paw.services.exceptions.UserEmailNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService extends BaseService<Long, User> {
    User create(User.UserBuilder userBuilder) throws UserEmailAlreadyTakenException;

    Optional<User> getUserWithEmail(String email);

    List<User> getAllWithImage();

    Optional<User> findByIdWithImage(Long id);

   Integer getUserSubjectProgress(Long id, String idSub);
    Map<String, Integer> getUserAllSubjectProgress(Long id);
    Integer deleteUserProgressForSubject(Long id, String idSub);
    Integer updateSubjectProgress(Long id, String idSub, Integer newProgress);
    void changePassword(Long userId, String password, String oldPassword, String userOldPassword) throws OldPasswordDoesNotMatchException;
    void editProfile(Long userId, String username);

    byte[] updateProfilePicture(long id, byte[] image);

    String generateRecoveryToken(String email);
    String generateRecoveryToken(User user);
    String generateRecoveryToken(long userId);

    List<Roles> getUserRoles(Long userId);

    boolean isValidToken(String token);

    void recoverPassword(String token, String newPassword) throws InvalidTokenException;

    Integer addIdToUserRoles(Long roleId, Long userId);

    Integer updateUserRoles(Long roleId, Long userId);
}
