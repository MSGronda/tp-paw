package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.exceptions.OldPasswordDoesNotMatchException;
import ar.edu.itba.paw.services.exceptions.UserEmailAlreadyTakenException;

import java.sql.SQLException;
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
}
