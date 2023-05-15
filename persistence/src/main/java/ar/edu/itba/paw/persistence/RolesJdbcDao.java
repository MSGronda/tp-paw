package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class RolesJdbcDao implements RolesDao{

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Roles> ROW_MAPPER = RolesJdbcDao::rowMapper;

    private final String ROLES_TABLE = "roles";

    @Autowired
    public RolesJdbcDao(final DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    private static Roles rowMapper(final ResultSet rs, final int rowNum) throws SQLException{
        return new Roles(
                rs.getLong("id"),
                rs.getString("name")
        );
    }


    @Override
    public Optional<Roles> findById(final Long id) {
        return jdbcTemplate.query("SELECT * FROM " + ROLES_TABLE + " WHERE id = ?", ROW_MAPPER, id ).stream().findFirst();
    }

    @Override
    public List<Roles> getAll() {
        return jdbcTemplate.query("SELECT * FROM " + ROLES_TABLE, ROW_MAPPER);
    }

    @Override
    public Optional<Roles> findByName(final String name) {
        return jdbcTemplate.query("SELECT * FROM " + ROLES_TABLE + " WHERE name = ?", ROW_MAPPER, name).stream().findFirst();
    }
}
