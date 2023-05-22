package ar.edu.itba.paw.persistence.dao.jdbc;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Semester;
import ar.edu.itba.paw.persistence.constants.Tables;
import ar.edu.itba.paw.persistence.dao.DegreeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(DegreeJdbcDao.class);

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public DegreeJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(Tables.DEGREES)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Degree> findById(final Long id) {
        Optional<Degree> maybeDegree = jdbcTemplate.query("SELECT * FROM " + Tables.DEGREES + " WHERE id = ?", DegreeJdbcDao::rowMapper, id)
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
        return jdbcTemplate.query("SELECT * FROM " + Tables.DEGREES, DegreeJdbcDao::rowMapper)
                .stream().map((deg) ->
                    new Degree(
                            deg.getId(),
                            deg.getName(),
                            getSemesters(deg.getId())
                    )
                ).collect(Collectors.toList());
    }

    @Override
    public Optional<Integer> getSubjectSemesterForDegree(final String subId){
        return jdbcTemplate.query("SELECT semester FROM " + Tables.SUBJECTS_DEGREES + " WHERE iddeg = 1 AND idsub = ?", DegreeJdbcDao::rowMapperSemesterDegree, subId).stream().findFirst();
    }

    @Override
    public Optional<Degree> getByName(final String name){
        return jdbcTemplate.query("SELECT * FROM " + Tables.DEGREES + " WHERE degname = ?", DegreeJdbcDao::rowMapper, name).stream().findFirst();
    }

    @Override
    public void insert(final Degree degree) {
        create(degree.getName());
    }

    @Override
    public void delete(final Long aLong) {

    }

    public Degree create(final String name) {
        Map<String, Object> data = new HashMap<>();
        data.put("degname", name);

        Number key = jdbcInsert.executeAndReturnKey(data);

        LOGGER.info("Degree created with name {}", name);
        return new Degree(key.longValue(), name, new ArrayList<>());
    }

    public void update(final Degree degree) {

    }

    private List<Semester> getSemesters(final long degId) {
        List<Integer> semesterNumbers = jdbcTemplate.query("SELECT DISTINCT semester FROM " + Tables.SUBJECTS_DEGREES + " WHERE iddeg = ?", DegreeJdbcDao::rowMapperSemesterDegree, degId);

        final List<Semester> semesters = new ArrayList<>();
        for (Integer num : semesterNumbers) {
            semesters.add(new Semester(
                    num, degId, getSubjectsBySemester(degId, num)
            ));
        }
        return semesters;
    }

    private List<String> getSubjectsBySemester(final long degId, final int semesterNumber) {
        return jdbcTemplate.query("SELECT * FROM " + Tables.SUBJECTS_DEGREES + " WHERE iddeg = ? AND semester = ?", DegreeJdbcDao::subIdRowMapper, degId, semesterNumber);
    }

    private static String subIdRowMapper(final ResultSet rs, final int rowNum) throws SQLException {
        return rs.getString("idsub");
    }

    private static int rowMapperSemesterDegree(final ResultSet rs, final int rowNum) throws SQLException {
        return rs.getInt("semester");
    }

    private static Degree rowMapper(final ResultSet rs, final int rowNum) throws SQLException {
        return new Degree(
                rs.getLong("id"),
                rs.getString("degname"),
                new ArrayList<>()
        );
    }
}
