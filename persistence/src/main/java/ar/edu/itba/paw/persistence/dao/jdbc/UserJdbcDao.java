package ar.edu.itba.paw.persistence.dao.jdbc;

import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.constants.Tables;
import ar.edu.itba.paw.persistence.dao.UserDao;
import ar.edu.itba.paw.persistence.exceptions.UserEmailAlreadyTakenPersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

//@Repository
public class UserJdbcDao implements UserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserJdbcDao.class);

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final SimpleJdbcInsert jdbcUserProgressInsert;
    private final SimpleJdbcInsert jdbcUserRolesInsert;


    private static final int NO_ROWS_AFFECTED = 0;

    @Autowired
    public UserJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(Tables.USERS)
                .usingGeneratedKeyColumns("id");
        this.jdbcUserProgressInsert = new SimpleJdbcInsert(ds)
                .withTableName(Tables.USER_SUBJECT_PROGRESS);
        this.jdbcUserRolesInsert = new SimpleJdbcInsert(ds)
                .withTableName(Tables.USER_ROLES);
    }


    public Optional<User> findById(final Long id) {
        return jdbcTemplate.query("SELECT * FROM " + Tables.USERS + " WHERE id = ? AND confirmed = true", UserJdbcDao::rowMapper, id)
                .stream().findFirst();
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM " + Tables.USERS + " WHERE confirmed = true", UserJdbcDao::rowMapper);
    }


    @Override
    public void insert(final User user) {
        //create(user);
    }

    @Override
    public User create(final User user) throws UserEmailAlreadyTakenPersistenceException {
        if (!user.getConfirmToken().isPresent())
            throw new IllegalArgumentException("Confirm token must be present");

        Map<String, Object> data = new HashMap<>();
        data.put("email", user.getEmail());
        data.put("pass", user.getPassword());
        data.put("username", user.getUsername());
        data.put("image_id", user.getImageId());
        data.put("confirmtoken", user.getConfirmToken().get());
        data.put("confirmed", false);

        Number key;

        try {
            key = jdbcInsert.executeAndReturnKey(data);
        } catch (DuplicateKeyException e) {
            LOGGER.info("Duplicate key for user email {}", user.getEmail());
            throw new UserEmailAlreadyTakenPersistenceException();
        }

        User newUser = User.builderFrom(user)
                .id(key.longValue())
                .build();

        LOGGER.info("User created with id {} and email {}", newUser.getId(), newUser.getEmail());
        return newUser;
    }

    @Override
    public void updateConfirmToken(long userId, String token) {
        jdbcTemplate.update("UPDATE " + Tables.USERS + " SET confirmtoken = ? WHERE id = ?", token, userId);
    }

    @Override
    public void delete(final Long id) {

    }

    @Override
    public void update(final User user) {

    }


    // - - - - - - - - - Subject Progress - - - - - - - - -
    @Override
    public Optional<Integer> getUserSubjectProgress(final Long id, final String idSub) {
        return jdbcTemplate.query("SELECT * FROM " + Tables.USER_SUBJECT_PROGRESS + " WHERE idUser = ? AND idSub = ?",
                UserJdbcDao::rowMapperUserSubjectProgress, id, idSub).stream().findFirst();
    }

    @Override
    public Map<String, Integer> getUserAllSubjectProgress(final Long id) {
        return jdbcTemplate.query("SELECT * FROM " + Tables.USER_SUBJECT_PROGRESS + " WHERE idUser = ?",
                UserJdbcDao::userAllSubjectsProgressExtractor, id);
    }

    @Override
    public Integer updateSubjectProgress(final Long id, final String idSub, final Integer newProgress) {
        int toReturn;
        if (getUserSubjectProgress(id, idSub).isPresent()) {
            toReturn = jdbcTemplate.update("UPDATE " + Tables.USER_SUBJECT_PROGRESS + " SET subjectState = ? WHERE idSub = ? AND idUser = ?",
                    newProgress,idSub,id);
            if(toReturn != NO_ROWS_AFFECTED) {
                LOGGER.info("Updating subject {} progress for user {}", idSub, id);
            } else {
                LOGGER.warn("Failed to update subject {} progress for user {}", idSub, id);
            }
        } else {
            Map<String, Object> data = new HashMap<>();

            data.put("idUser", id);
            data.put("idSub", idSub);
            data.put("subjectState", newProgress);
            toReturn = jdbcUserProgressInsert.execute(data);
            if(toReturn != NO_ROWS_AFFECTED) {
                LOGGER.info("Generated subject progress in {} for user {}", idSub, id);
            } else {
                LOGGER.warn("Failed to generate subject progress in {} for user {}", idSub, id);
            }
        }
        return toReturn;
    }

    @Override
    public Integer deleteUserProgressForSubject(final Long id, final String idSub){
        int toReturn = jdbcTemplate.update("DELETE FROM " + Tables.USER_SUBJECT_PROGRESS + " WHERE idSub = ? AND idUser = ?", idSub,id);
        if(toReturn != NO_ROWS_AFFECTED) {
            LOGGER.info("Deleted subject progress in {} for user {}", idSub, id);
        } else {
            LOGGER.warn("Progress delete in subject {} for user {} failed", idSub, id);
        }
        return toReturn;
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public Optional<User> getUserWithEmail(final String email) {
        return jdbcTemplate.query("SELECT * FROM " + Tables.USERS + " WHERE email = ? AND confirmed = true", UserJdbcDao::rowMapper, email).stream().findFirst();
    }

    @Override
    public Optional<User> getUnconfirmedUserWithEmail(final String email) {
        return jdbcTemplate.query("SELECT * FROM " + Tables.USERS + " WHERE email = ? AND confirmed = false", UserJdbcDao::rowMapper, email).stream().findFirst();
    }

    private static Map<String, Integer> userAllSubjectsProgressExtractor(final ResultSet rs) throws SQLException {
        final Map<String, Integer> res = new HashMap<>();

        while (rs.next()) {
            String idSub = rs.getString("idSub");
            Integer state = rs.getInt("subjectState");
            res.put(idSub, state);
        }
        return res;
    }

    @Override
    public void changePassword(final Long userId, final String password) {
        int toReturn = jdbcTemplate.update("UPDATE " + Tables.USERS + " SET pass = ? WHERE id = ?", password, userId);
        if(toReturn != NO_ROWS_AFFECTED) {
            LOGGER.info("Changed password for user {}", userId);
        } else {
            LOGGER.warn("Password change for user {} failed", userId);
        }
    }

    @Override
    public void editProfile(final Long userId, final String username) {
        int toReturn = jdbcTemplate.update("UPDATE " + Tables.USERS + " SET username = ? WHERE id = ?", username, userId);
        if(toReturn != NO_ROWS_AFFECTED)
            LOGGER.info("Edited username for user {}", username);
        else
            LOGGER.warn("Username edition for user {} failed", userId);
    }

    //-------------------------------------------------------------------------------------


    @Override
    public void setLocale(long userId, Locale locale) {
        jdbcTemplate.update("UPDATE " + Tables.USERS + " SET locale = ? WHERE id = ?", locale.toString(), userId);
    }

    //------------------------ User Roles ---------------------------------------------------
    @Override
    public List<Role> getUserRoles(final Long userId) {
        return jdbcTemplate.query("SELECT id,name FROM " + Tables.ROLES + " JOIN " + Tables.USER_ROLES + " ON id = roleId WHERE userId = ?", UserJdbcDao::rolesRowMapper, userId);
    }

    @Override
    public Integer addIdToUserRoles(final Long roleId, final Long userId) {
        Map<String, Long> data = new HashMap<>();
        data.put("roleId", roleId);
        data.put("userId", userId);
        int success = jdbcUserRolesInsert.execute(data);
        if (success != 0) {
            LOGGER.info("Added role {} to user with id {}", roleId, userId);
        } else {
            LOGGER.warn("Failed to add role {} to user with id {}", roleId, userId);
        }
        return success;
    }

    private static Role rolesRowMapper(final ResultSet rs, final int rowNum) throws SQLException {
        return new Role(
                rs.getLong("id"),
                rs.getString("name")
        );
    }

    public Integer updateUserRoles(final Long roleId, final Long userId) {
        int success = jdbcTemplate.update("UPDATE " + Tables.USER_ROLES + " SET roleid = ? WHERE userid = ?", roleId, userId);
        if (success != 0) {
            LOGGER.info("Updated user with id {} role to {}", userId, roleId);
        } else {
            LOGGER.warn("Failed to update user with id {} role to {}", userId, roleId);
        }
        return success;
    }

    //---------------------------------------------------------------------------------------

    @Override
    public Optional<User> findUserByConfirmToken(final String token) {
        return jdbcTemplate.query("SELECT * FROM " + Tables.USERS + " WHERE confirmtoken = ?", UserJdbcDao::rowMapper, token)
                .stream().findFirst();
    }

    @Override
    public void confirmUser(final long userId) {
        jdbcTemplate.update("UPDATE " + Tables.USERS + " SET confirmtoken = NULL, confirmed = true WHERE id = ?", userId);
    }

    private static User rowMapper(final ResultSet rs, final int rowNum) throws SQLException {
        final String localeString = rs.getString("locale");
        final Locale locale = localeString == null ? null : Locale.forLanguageTag(localeString);

        return User.builder()
                .email(rs.getString("email"))
                .password(rs.getString("pass"))
                .username(rs.getString("username"))
                .id(rs.getLong("id"))
                .imageId(rs.getLong("image_id"))
                .locale(locale)
                .confirmed(rs.getBoolean("confirmed"))
                .build();
    }

    private static Integer rowMapperUserSubjectProgress(final ResultSet rs, final int rowNum) throws SQLException {
        return rs.getInt("subjectState");
    }
}
