package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.exceptions.UserEmailAlreadyTakenPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserJdbcDao implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final SimpleJdbcInsert jdbcUserProgressInsert;

    private final String USERS_TABLE = "users";
    private final String USER_SUB_PRG_TABLE = "userSubjectProgress";

    @Autowired
    public UserJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
            .withTableName(USERS_TABLE)
            .usingGeneratedKeyColumns("id");
        this.jdbcUserProgressInsert = new SimpleJdbcInsert(ds)
                .withTableName(USER_SUB_PRG_TABLE);
    }



    public Optional<User> findById(Long id) {
        return jdbcTemplate.query("SELECT * FROM " + USERS_TABLE + " WHERE id = ?", UserJdbcDao::rowMapper, id)
            .stream().findFirst();
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM " + USERS_TABLE, UserJdbcDao::rowMapper);
    }


    @Override
    public void insert(User user) {
        //create(user);
    }

    @Override
    public User create(User.UserBuilder userBuilder) throws UserEmailAlreadyTakenPersistenceException {

        Map<String, Object> data = new HashMap<>();
        data.put("email", userBuilder.getEmail());
        data.put("pass", userBuilder.getPassword());
        data.put("username", userBuilder.getUsername());
        data.put("image", userBuilder.getImage());

        Number key;

        try{
            key = jdbcInsert.executeAndReturnKey(data);
        }catch (DuplicateKeyException e){
            throw new UserEmailAlreadyTakenPersistenceException();
        }

        return userBuilder.id(key.longValue()).build();
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void update(User user) {

    }

    // - - - - - - - - - User With Image - - - - - - - - -
    @Override
    public byte[] updateProfilePicture(long id, byte[] image) {
        jdbcTemplate.update("UPDATE "+ USERS_TABLE + " SET image = ? WHERE id = ?", image, id);
        return image;
    }
    @Override
    public Optional<User> findByIdWithImage(Long id){
        return jdbcTemplate.query("SELECT * FROM " + USERS_TABLE + " WHERE id = ?", UserJdbcDao::rowMapperWithImage, id)
                .stream().findFirst();
    }
    @Override
    public List<User> getAllWithImage() {
        return jdbcTemplate.query("SELECT * FROM " + USERS_TABLE, UserJdbcDao::rowMapperWithImage);
    }
    private static User rowMapperWithImage(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                new User.UserBuilder(rs.getString("email"),
                        rs.getString("pass"),
                        rs.getString("username"))
                        .id(rs.getLong("id")).image(rs.getBytes("image"))
        );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - -


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
    public void updateSubjectProgress(Long id, String idSub, Integer newProgress){
        if(getUserSubjectProgress(id,idSub).isPresent()){
            jdbcTemplate.update("UPDATE " + USER_SUB_PRG_TABLE + " SET subjectState = ? WHERE idSub = ? AND idUser = ?",
                    newProgress,idSub,id);
        }
        else{
            Map<String, Object> data = new HashMap<>();

            data.put("idUser",id);
            data.put("idSub",idSub);
            data.put("subjectState",newProgress);

            jdbcUserProgressInsert.execute(data);
        }
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public Optional<User> getUserWithEmail(String email){
        return jdbcTemplate.query("SELECT * FROM " + USERS_TABLE + " WHERE email = ?", UserJdbcDao::rowMapper, email).stream().findFirst();
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

    private static Integer rowMapperUserSubjectProgress(ResultSet rs , int rowNum) throws SQLException {
        return rs.getInt("subjectState");
    }

    @Override
    public void changePassword(Long userId, String password) {
        jdbcTemplate.update("UPDATE " + USERS_TABLE + " SET pass = ? WHERE id = ?", password, userId);
    }

    @Override
    public void editProfile(Long userId, String username) {
        jdbcTemplate.update("UPDATE " + USERS_TABLE + " SET username = ? WHERE id = ?", username, userId);
    }

    private static User rowMapper(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                new User.UserBuilder(rs.getString("email"),
                        rs.getString("pass"),
                        rs.getString("username"))
                        .id(rs.getLong("id"))
        );
    }
}
