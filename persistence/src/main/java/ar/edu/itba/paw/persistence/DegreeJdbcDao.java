package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.models.Degree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository

public class DegreeJdbcDao implements DegreeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static String degree_table_name = "degree";
    private static final RowMapper<Degree> ROW_MAPPER = DegreeJdbcDao::rowMapper;



    @Autowired
    public DegreeJdbcDao(final DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(degree_table_name)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Degree> findById(Long id) {
        return jdbcTemplate.query("SELECT * FROM degree WHERE id = ?", ROW_MAPPER, id)
                .stream().findFirst();
    }

    @Override
    public List<Degree> getAll() {
        return jdbcTemplate.query("SELECT * FROM degree",ROW_MAPPER);
    }

    @Override
    public void insert(Degree degree) {
        create(degree.getName());
    }

    @Override
    public void delete(Long aLong) {

    }

    private static Degree rowMapper(ResultSet rs, int rowNum) throws SQLException {
        return new Degree (
                rs.getLong("id"),
                rs.getString("name"),
                new ArrayList<>()
        );
    }
    public Degree create(String name) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);

        Number key = jdbcInsert.executeAndReturnKey(data);

        return new Degree(key.longValue(), name, new ArrayList<>());
    }

    public void update(Degree degree) {

    }
}
