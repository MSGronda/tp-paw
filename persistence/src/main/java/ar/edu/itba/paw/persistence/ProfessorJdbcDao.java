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
    private static final RowMapper<Long> ROW_MAPPER_MATPROF = ProfessorJdbcDao::rowMapperMatProf;

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertProfesor;
    private final SimpleJdbcInsert jdbcInsertMateriaProfesor;

    private final String profTable = "professor";
    private final String matProfTable = "professorSubjects";


    private static Professor rowMapperProf(ResultSet rs, int rowNum) throws SQLException {
        return new Professor(
                rs.getLong("Id"),
                rs.getString("Nombre")
        );
    }
    private static Long rowMapperMatProf(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("idMat");
    }

    @Autowired
    public ProfessorJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsertProfesor = new SimpleJdbcInsert(ds)
                .withTableName(profTable)
                .usingGeneratedKeyColumns("Id");

        this.jdbcInsertMateriaProfesor = new SimpleJdbcInsert(ds)
                .withTableName(matProfTable)
                .usingGeneratedKeyColumns("idProf");
    }

    // No incluye las materias que enseña, el atributo es null
    @Override
    public Optional<Professor> findByIdWithoutSubjects(Long id) {
        return jdbcTemplate.query("SELECT * FROM " + profTable + " WHERE id = ?", ROW_MAPPER_PROF, id).stream().findFirst();
    }

    // Dado un ID de prof, busco todos los ids de las materias en cual esta ese prof
    public Optional<List<Long>> findSubjectsByIdProfesor(Long id) {
        List<Long> list = jdbcTemplate.query("SELECT * FROM " + matProfTable + " WHERE idProf = ?", ROW_MAPPER_MATPROF, id);

        return  Optional.of(list);
    }

    // Incluye una lista de todas las materias que esta enseñando el profesor.
    @Override
    public Optional<Professor> findById(Long id) {
        Optional<Professor> resp = findByIdWithoutSubjects(id);

        Optional<List<Long>> materias = findSubjectsByIdProfesor(id);

        // TODO fijate si esto tiene sentido
        if(resp.isPresent()){
            if(materias.isPresent())
                resp.get().setSubjectIds(materias.get());
            else
                resp.get().setSubjectIds(new ArrayList<>());
        }

        return resp;
    }

    @Override
    public void insert(Professor professor) {
        create(professor.getName(), professor.getSubjectIds());
    }

    @Override
    public Professor create(String name , List<Long> materias) {
        Map<String, Object> data = new HashMap<>();
        data.put("Nombre", name);

        Number key = jdbcInsertProfesor.executeAndReturnKey(data);

        // Inserto en la tabla de Mat_Prof las materias que hace un profesor
        for(Long materia : materias){
            Map<String, Object> matProf = new HashMap<>();
            data.put("idProf", key);
            data.put("idMat", materia);

            jdbcInsertMateriaProfesor.executeAndReturnKey(matProf);
        }

        return new Professor(key.longValue(), name, materias);
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
