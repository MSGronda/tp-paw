package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final String USERS_TABLE = "users";

    @Autowired
    public UserJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
            .withTableName("users")
            .usingGeneratedKeyColumns("id");
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
    public User create(User.UserBuilder userBuilder) {
        Map<String, Object> data = new HashMap<>();
        data.put("email", userBuilder.getEmail());
        data.put("pass", userBuilder.getPassword());
        data.put("username", userBuilder.getUsername());

        Number key = jdbcInsert.executeAndReturnKey(data);

        return userBuilder.id(key.longValue()).build();
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public Optional<User> getUserWithEmail(String email){
        return jdbcTemplate.query("SELECT * FROM " + USERS_TABLE + " WHERE email = ?", UserJdbcDao::rowMapper, email).stream().findFirst();
    }

    @Override
    public void changePassword(String email, String password) {
        jdbcTemplate.update("UPDATE " + USERS_TABLE + " SET pass = ? WHERE email = ?", password, email);
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
