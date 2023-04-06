package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Professor;
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
public class ProfessorJdbcDao implements ProfessorDao {
    private static final RowMapper<Professor> ROW_MAPPER_PROF = ProfessorJdbcDao::rowMapperProf;
    private static final RowMapper<Long> ROW_MAPPER_MATPROF = ProfessorJdbcDao::rowMapperSubId;
    private static final String TABLE_PROF = "professors";
    private static final String TABLE_PROF_SUB = "professorsSubjects";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertProfesor;
    private final SimpleJdbcInsert jdbcInsertMateriaProfesor;




    private static Professor rowMapperProf(ResultSet rs, int rowNum) throws SQLException {
        return new Professor(
                rs.getLong("id"),
                rs.getString("profName")
        );
    }
    private static Long rowMapperSubId(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("idSub");
    }

    @Autowired
    public ProfessorJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsertProfesor = new SimpleJdbcInsert(ds)
                .withTableName(TABLE_PROF)
                .usingGeneratedKeyColumns("id");

        this.jdbcInsertMateriaProfesor = new SimpleJdbcInsert(ds)
                .withTableName(TABLE_PROF_SUB)
                .usingGeneratedKeyColumns("idProf", "idSub");
    }

    // No incluye las materias que enseña, el atributo es null
    private Optional<Professor> findByIdRaw(Long id) {
        return jdbcTemplate.query("SELECT * FROM " + TABLE_PROF + " WHERE id = ?", ROW_MAPPER_PROF, id).stream().findFirst();
    }

    // Dado un ID de prof, busco todos los ids de las materias en cual esta ese prof
    private List<Long> findSubjects(Long id) {
        return jdbcTemplate.query("SELECT * FROM " + TABLE_PROF_SUB + " WHERE idProf = ?", ROW_MAPPER_MATPROF, id);
    }

    // Incluye una lista de todas las materias que esta enseñando el profesor.
    @Override
    public Optional<Professor> findById(Long id) {
        Optional<Professor> optProf = findByIdRaw(id);
        if(!optProf.isPresent()) return Optional.empty();

        Professor prof = optProf.get();

        List<Long> subjects = findSubjects(id);

        Professor inflatedProf = new Professor(
            prof.getId(),
            prof.getName(),
            subjects
        );
        return Optional.of(inflatedProf);
    }

    @Override
    public void insert(Professor professor) {
        create(professor.getName(), professor.getSubjectIds());
    }

    @Override
    public Professor create(String name , List<Long> subjects) {
        Map<String, Object> data = new HashMap<>();
        data.put("subName", name);

        Number key = jdbcInsertProfesor.executeAndReturnKey(data);

        // Inserto en la tabla de Mat_Prof las materias que hace un profesor
        for(Long subject : subjects){
            Map<String, Object> matProf = new HashMap<>();
            data.put("idProf", key);
            data.put("idSub", subject);

            jdbcInsertMateriaProfesor.executeAndReturnKey(matProf);
        }

        return new Professor(key.longValue(), name, subjects);
    }


    public List<Professor> getAll() {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void update(Professor professor) {

    }
}
