package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Semester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository

public class DegreeJdbcDao implements DegreeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static String degree_table_name = "degrees";
    private final static String subdegree_table_name = "subjectsDegrees";


    @Autowired
    public DegreeJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(degree_table_name)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Degree> findById(Long id) {
        Optional<Degree> maybeDegree = jdbcTemplate.query("SELECT * FROM " + degree_table_name + " WHERE id = ?", DegreeJdbcDao::rowMapper, id)
                .stream().findFirst();

        if (!maybeDegree.isPresent()) return Optional.empty();
        Degree deg = maybeDegree.get();

        return Optional.of(new Degree(
                deg.getId(),
                deg.getName(),
                getSemesters(deg.getId())
        ));
    }

    @Override
    public List<Degree> getAll() {
        return jdbcTemplate.query("SELECT * FROM " + degree_table_name, DegreeJdbcDao::rowMapper)
                .stream().map((deg) ->
                    new Degree(
                            deg.getId(),
                            deg.getName(),
                            getSemesters(deg.getId())
                    )
                ).collect(Collectors.toList());
    }

    @Override
    public void insert(Degree degree) {
        create(degree.getName());
    }

    @Override
    public void delete(Long aLong) {

    }

    public Degree create(String name) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);

        Number key = jdbcInsert.executeAndReturnKey(data);

        return new Degree(key.longValue(), name, new ArrayList<>());
    }

    public void update(Degree degree) {

    }

    private List<Semester> getSemesters(long degId) {
        List<Integer> semesterNumbers = jdbcTemplate.query("SELECT DISTINCT semester FROM " + subdegree_table_name + " WHERE iddeg = ?", DegreeJdbcDao::semesterCountRowMapper, degId);

        final List<Semester> semesters = new ArrayList<>();
        for (Integer num : semesterNumbers) {
            semesters.add(new Semester(
                    num, degId, getSubjectsBySemester(degId, num)
            ));
        }

        return semesters;
    }

    private List<String> getSubjectsBySemester(long degId, int semesterNumber) {
        return jdbcTemplate.query("SELECT * FROM " + subdegree_table_name + " WHERE iddeg = ? AND semester = ?", DegreeJdbcDao::subIdRowMapper, degId, semesterNumber);
    }

    private static String subIdRowMapper(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("idsub");
    }

    private static Degree rowMapper(ResultSet rs, int rowNum) throws SQLException {
        return new Degree(
                rs.getLong("id"),
                rs.getString("degname"),
                new ArrayList<>()
        );
    }

    private static Integer semesterCountRowMapper(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt(1);
    }
}
