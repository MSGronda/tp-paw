package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Roles;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.exceptions.UserEmailAlreadyTakenPersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserJdbcDao implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final SimpleJdbcInsert jdbcUserProgressInsert;
    private final ImageDao imageDao;

    private final SimpleJdbcInsert jdbcUserRolesInsert;

    private final String USERS_TABLE = "users";
    private final String USER_SUB_PRG_TABLE = "userSubjectProgress";
    private static final Logger LOGGER = LoggerFactory.getLogger(UserJdbcDao.class);

    private final String USER_ROLES_TABLE = "userroles";
    private final String ROLES_TABLE = "roles";

    @Autowired
    public UserJdbcDao(final DataSource ds, final ImageDao imageDao) {
        this.imageDao = imageDao;
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
            .withTableName(USERS_TABLE)
            .usingGeneratedKeyColumns("id");
        this.jdbcUserProgressInsert = new SimpleJdbcInsert(ds)
                .withTableName(USER_SUB_PRG_TABLE);
        this.jdbcUserRolesInsert = new SimpleJdbcInsert(ds)
                .withTableName(USER_ROLES_TABLE);
    }



    public Optional<User> findById(Long id) {
        return jdbcTemplate.query("SELECT * FROM " + USERS_TABLE + " WHERE id = ? AND confirmed = true", UserJdbcDao::rowMapper, id)
            .stream().findFirst();
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM " + USERS_TABLE + " WHERE confirmed = true", UserJdbcDao::rowMapper);
    }


    @Override
    public void insert(User user) {
        //create(user);
    }

    @Override
    public User create(User.UserBuilder userBuilder, String confirmToken) throws UserEmailAlreadyTakenPersistenceException {
        Map<String, Object> data = new HashMap<>();
        data.put("email", userBuilder.getEmail());
        data.put("pass", userBuilder.getPassword());
        data.put("username", userBuilder.getUsername());
        data.put("image_id", userBuilder.getImageId());
        data.put("confirmtoken", confirmToken);
        data.put("confirmed", false);

        Number key;

        try{
            key = jdbcInsert.executeAndReturnKey(data);
        }catch (DuplicateKeyException e){
            LOGGER.warn("Duplicate key for user email {}",userBuilder.getEmail());
            throw new UserEmailAlreadyTakenPersistenceException();
        }

        User toReturn = userBuilder.id(key.longValue()).build();
        LOGGER.info("User created with id {} and email {}", toReturn.getId(), toReturn.getEmail());
        return toReturn;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void update(User user) {

    }


    // - - - - - - - - - Subject Progress - - - - - - - - -
    @Override
    public Optional<Integer> getUserSubjectProgress(Long id, String idSub) {
        return jdbcTemplate.query("SELECT * FROM " + USER_SUB_PRG_TABLE + " WHERE idUser = ? AND idSub = ?",
                UserJdbcDao::rowMapperUserSubjectProgress, id, idSub).stream().findFirst();
    }
    @Override
    public Map<String, Integer> getUserAllSubjectProgress(Long id) {
        return jdbcTemplate.query("SELECT * FROM " + USER_SUB_PRG_TABLE + " WHERE idUser = ?",
                UserJdbcDao::userAllSubjectsProgressExtractor, id);
    }
    @Override
    public Integer updateSubjectProgress(Long id, String idSub, Integer newProgress){
        int toReturn;
        if(getUserSubjectProgress(id,idSub).isPresent()){
            toReturn = jdbcTemplate.update("UPDATE " + USER_SUB_PRG_TABLE + " SET subjectState = ? WHERE idSub = ? AND idUser = ?",
                    newProgress,idSub,id);
            if(toReturn != 0) {
                LOGGER.info("Updating subject {} progress for user {}", idSub, id);
            } else {
               LOGGER.warn("Failed to update subject {} progress for user {}", idSub, id);
            }
        }
        else{
            Map<String, Object> data = new HashMap<>();

            data.put("idUser",id);
            data.put("idSub",idSub);
            data.put("subjectState",newProgress);
            toReturn = jdbcUserProgressInsert.execute(data);
            if(toReturn != 0) {
                LOGGER.info("Generated subject progress in {} for user {}", idSub, id);
            } else {
                LOGGER.warn("Failed to generate subject progress in {} for user {}", idSub, id);
            }
        }
        return toReturn;
    }

    @Override
    public Integer deleteUserProgressForSubject(Long id, String idSub){
        int toReturn = jdbcTemplate.update("DELETE FROM " + USER_SUB_PRG_TABLE + " WHERE idSub = ? AND idUser = ?", idSub,id);
        if(toReturn !=0 ) {
            LOGGER.info("Deleted subject progress in {} for user {}", idSub, id);
        } else {
            LOGGER.warn("Progress delete in subject {} for user {} failed", idSub, id);
        }
        return toReturn;
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public Optional<User> getUserWithEmail(String email){
        return jdbcTemplate.query("SELECT * FROM " + USERS_TABLE + " WHERE email = ? AND confirmed = true", UserJdbcDao::rowMapper, email).stream().findFirst();
    }

    private static Map<String,Integer> userAllSubjectsProgressExtractor(ResultSet rs) throws SQLException {
        final Map<String, Integer> res = new HashMap<>();

        while(rs.next()){
            String idSub = rs.getString("idSub");
            Integer state = rs.getInt("subjectState");
            res.put(idSub,state);
        }
        return res;
    }

    @Override
    public void changePassword(Long userId, String password) {
        int success = jdbcTemplate.update("UPDATE " + USERS_TABLE + " SET pass = ? WHERE id = ?", password, userId);
        if(success != 0) {
            LOGGER.info("Changed password for user {}", userId);
        } else {
            LOGGER.warn("Password change for user {} failed", userId);
        }
    }

    @Override
    public void editProfile(Long userId, String username) {
        int success = jdbcTemplate.update("UPDATE " + USERS_TABLE + " SET username = ? WHERE id = ?", username, userId);
        if(success != 0)
            LOGGER.info("Edited username for user {}", username);
        else
            LOGGER.warn("Username edition for user {} failed", userId);
    }

    //-------------------------------------------------------------------------------------

    //------------------------ User Roles ---------------------------------------------------
    @Override
    public List<Roles> getUserRoles(Long userId){
        return jdbcTemplate.query("SELECT id,name FROM " + ROLES_TABLE + " FULL JOIN " + USER_ROLES_TABLE + " ON id = roleId WHERE userId = ?", UserJdbcDao::rolesRowMapper, userId);
    }

    @Override
    public Integer addIdToUserRoles(Long roleId, Long userId){
        Map<String, Long> data = new HashMap<>();
        data.put("roleId", roleId);
        data.put("userId", userId);
        int success = jdbcUserRolesInsert.execute(data);
        if(success != 0) {
            LOGGER.info("Added role {} to user with id {}", roleId, userId);
        } else {
            LOGGER.warn("Failed to add role {} to user with id {}", roleId, userId);
        }
        return success;
    }

    private static Roles rolesRowMapper(ResultSet rs, int rowNum) throws SQLException {
        return new Roles (
                rs.getLong("id"),
                rs.getString("name")
        );
    }

    public Integer updateUserRoles(Long roleId, Long userId) {
        int success = jdbcTemplate.update("UPDATE " + USER_ROLES_TABLE + " SET roleid = ? WHERE userid = ?", roleId, userId);
        if(success != 0) {
            LOGGER.warn("Updated user with id {} role to {}", userId, roleId);
        } else {
            LOGGER.warn("Failed to update user with id {} role to {}", userId, roleId);
        }
        return success;
    }

    //---------------------------------------------------------------------------------------

    @Override
    public Optional<User> findUserByConfirmToken(String token) {
        return jdbcTemplate.query("SELECT * FROM " + USERS_TABLE + " WHERE confirmtoken = ?", UserJdbcDao::rowMapper, token)
                .stream().findFirst();
    }

    @Override
    public void confirmUser(long userId) {
        jdbcTemplate.update("UPDATE " + USERS_TABLE + " SET confirmtoken = NULL, confirmed = true WHERE id = ?", userId);
    }

    private static User rowMapper(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                new User.UserBuilder(rs.getString("email"),
                        rs.getString("pass"),
                        rs.getString("username")
                )
                    .id(rs.getLong("id"))
                    .imageId(rs.getLong("image_id"))
        );
    }

    private static Integer rowMapperUserSubjectProgress(ResultSet rs , int rowNum) throws SQLException {
        return rs.getInt("subjectState");
    }
}
