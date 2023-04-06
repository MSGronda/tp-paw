package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
    private static final RowMapper<User> ROW_MAPPER = UserJdbcDao::rowMapper;

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public UserJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
            .withTableName("users")
            .usingGeneratedKeyColumns("id");
    }

    public Optional<User> findById(Long id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE id = ?", ROW_MAPPER, id)
            .stream().findFirst();
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users", ROW_MAPPER);
    }

    @Override
    public void insert(User user) {
        create(user.getEmail(), user.getPassword(), user.getUsername());
    }

    @Override
    public User create(String email, String password, String name) {
        Map<String, Object> data = new HashMap<>();
        data.put("email", email);
        data.put("pass", password);
        data.put("username", name);

        Number key = jdbcInsert.executeAndReturnKey(data);

        return new User(key.longValue(), email, password, name);
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void update(User user) {

    }

    private static User rowMapper(ResultSet rs, int rowNum) throws SQLException {
        return new User(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("pass"),
            rs.getString("username")
        );
    }
}
