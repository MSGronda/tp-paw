package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.persistence.constants.Tables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class RecoveryJdbcDao implements RecoveryDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecoveryJdbcDao.class);

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public RecoveryJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(Tables.RECOVERY);
    }

    @Override
    public void create(final String token, final long userId) {
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userid", userId);

        jdbcTemplate.update(
                "INSERT INTO " + Tables.RECOVERY + " (token, userid) VALUES (?, ?) " +
                        "ON CONFLICT(userid) DO UPDATE SET token = excluded.token",
                token, userId
        );
        LOGGER.info("Created recovery password token for user {}", userId);
    }

    @Override
    public Optional<Long> findUserIdByToken(final String token) {
        return jdbcTemplate.query("SELECT * FROM " + Tables.RECOVERY + " WHERE token = ?", RecoveryJdbcDao::userIdRowMapper, token)
                .stream()
                .findFirst();
    }

    @Override
    public void delete(final String token) {
        jdbcTemplate.update("DELETE FROM " + Tables.RECOVERY + " WHERE token = ?", token);
        LOGGER.info("Deleted token {} from recovery token table", token);
    }

    @Override
    public void delete(final long userId) {
        jdbcTemplate.update("DELETE FROM " + Tables.RECOVERY + " WHERE userid = ?", userId);
        LOGGER.info("Deleted user from recovery token table with id {}", userId);
    }

    private static Long userIdRowMapper(final ResultSet rs, final int rowNum) throws SQLException {
        return rs.getLong("userid");
    }
}
