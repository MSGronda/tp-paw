package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Roles;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.exceptions.UserEmailAlreadyTakenPersistenceException;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserDao extends RWDao<Long,User> {
    User create(User.UserBuilder userBuilder) throws UserEmailAlreadyTakenPersistenceException;

    Optional<User> findByIdWithImage(Long id);

    List<User> getAllWithImage();

    byte[] updateProfilePicture(long id, byte[] image);

    Optional<User> getUserWithEmail(final String email);

    Integer deleteUserProgressForSubject(Long id, String idSub);
    Integer updateSubjectProgress(Long id, String idSub, Integer newProgress);
    Optional<Integer> getUserSubjectProgress(Long id, String idSub);
    Map<String, Integer> getUserAllSubjectProgress(Long id);

    void changePassword(Long userId, String password);

    void editProfile(Long userId, String username);

    List<Roles> getUserRoles(Long userId);

    Integer addIdToUserRoles(Long roleId, Long userId);

    Integer updateUserRoles(Long roleId, Long userId);
}
