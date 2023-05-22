package ar.edu.itba.paw.persistence.dao.jdbc;

import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.persistence.constants.Tables;
import ar.edu.itba.paw.persistence.dao.RolesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class RolesJdbcDao implements RolesDao {
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public RolesJdbcDao(final DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
    }


    @Override
    public Optional<Role> findById(final Long id) {
        return jdbcTemplate.query("SELECT * FROM " + Tables.ROLES + " WHERE id = ?", RolesJdbcDao::rowMapper, id ).stream().findFirst();
    }

    @Override
    public List<Role> getAll() {
        return jdbcTemplate.query("SELECT * FROM " + Tables.ROLES, RolesJdbcDao::rowMapper);
    }

    @Override
    public Optional<Role> findByName(final String name) {
        return jdbcTemplate.query("SELECT * FROM " + Tables.ROLES + " WHERE name = ?", RolesJdbcDao::rowMapper, name).stream().findFirst();
    }

    private static Role rowMapper(final ResultSet rs, final int rowNum) throws SQLException{
        return new Role(
            rs.getLong("id"),
            rs.getString("name")
        );
    }
}
