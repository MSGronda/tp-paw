package ar.edu.itba.paw.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class RecoveryJdbcDao implements RecoveryDao {
    private static final String TABLE = "recoveryToken";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public RecoveryJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(TABLE);
    }

    @Override
    public void create(String token, long userId) {
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userid", userId);

        jdbcTemplate.update(
                "INSERT INTO " + TABLE + " (token, userid) VALUES (?, ?) " +
                        "ON CONFLICT(userid) DO UPDATE SET token = excluded.token",
                token, userId
        );
    }

    @Override
    public Optional<Long> findUserIdByToken(String token) {
        return jdbcTemplate.query("SELECT * FROM " + TABLE + " WHERE token = ?", RecoveryJdbcDao::userIdRowMapper, token)
                .stream()
                .findFirst();
    }

    @Override
    public void delete(String token) {
        jdbcTemplate.update("DELETE FROM " + TABLE + " WHERE token = ?", token);
    }

    @Override
    public void delete(long userId) {
        jdbcTemplate.update("DELETE FROM " + TABLE + " WHERE userid = ?", userId);
    }

    private static Long userIdRowMapper(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("userid");
    }
}
