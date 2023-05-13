package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Roles;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.exceptions.InvalidTokenException;
import ar.edu.itba.paw.services.exceptions.OldPasswordDoesNotMatchException;
import ar.edu.itba.paw.services.exceptions.UserEmailAlreadyTakenException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService extends BaseService<Long, User> {
    User create(User.UserBuilder userBuilder, byte[] profilePic) throws UserEmailAlreadyTakenException;
    User create(User.UserBuilder userBuilder) throws UserEmailAlreadyTakenException, IOException;

    Optional<User> getUserWithEmail(String email);

    Integer getUserSubjectProgress(Long id, String idSub);
    Map<String, Integer> getUserAllSubjectProgress(Long id);
    Integer deleteUserProgressForSubject(Long id, String idSub);
    Integer updateSubjectProgress(Long id, String idSub, Integer newProgress);
    void changePassword(Long userId, String password, String oldPassword, String userOldPassword) throws OldPasswordDoesNotMatchException;
    void editProfile(Long userId, String username);

    void updateProfilePicture(User user, byte[] image);

    String sendRecoveryMail(String email);

    List<Roles> getUserRoles(Long userId);

    boolean isValidRecoveryToken(String token);

    void recoverPassword(String token, String newPassword) throws InvalidTokenException;

    Integer addIdToUserRoles(Long roleId, Long userId);

    Integer updateUserRoles(Long roleId, Long userId);

    void confirmUser(String token) throws InvalidTokenException;
}
