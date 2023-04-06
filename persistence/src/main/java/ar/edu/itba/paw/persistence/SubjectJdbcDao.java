package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Subject;
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
public class SubjectJdbcDao implements SubjectDao {

    private static final RowMapper<Subject> ROW_MAPPER = SubjectJdbcDao::rowMapper;
    private static Subject rowMapper(ResultSet rs, int rowNum) throws SQLException {
        return new Subject(
                rs.getLong("id"),
                rs.getString("nombre"),
                rs.getString("depto"),
//                new ArrayList<>(), //TODO - query para lista de id de correlativas
//                new ArrayList<>(),  //TODO - query para lista de id de profesores
//                new ArrayList<>(),   //TODO - query para lista de id de carreras
                rs.getInt("creditos")
        );
    }

    private static final RowMapper<Long> ROW_MAPPER_PROFESORES = SubjectJdbcDao::rowMapperProfesores;

    //select * from professorSubject where idSub=?,id
    private static Long rowMapperProfesores(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("idProf");
    }

    private static final RowMapper<Long> ROW_MAPPER_CORRELATIVAS = SubjectJdbcDao::rowMapperCorrelativas;
    private static Long rowMapperCorrelativas(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("idCor");
    }

    private static final RowMapper<Long> ROW_MAPPER_CARRERAS = SubjectJdbcDao::rowMapperCarreras;
    private static Long rowMapperCarreras(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("idCar");
    }

    private static final String MATERIA = "subject";
    private static final String PROFESORMATERIA = "professorSubject";

    private static final String MATERIACORRELATIVA = "subjectCorrelative";
    private static final String MATERIACARRERA = "subjectCareer";


    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public SubjectJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(MATERIA)
                .usingGeneratedKeyColumns("id");
    }

    private Optional<Subject> findByIdWithoutAll(Long id){
        return jdbcTemplate.query("SELECT * FROM " + MATERIA + " WHERE id = ?", ROW_MAPPER, id)
                .stream().findFirst();
    }

    private List<Long> findCorrelativesWithId(Long id){
        List<Long> list = jdbcTemplate.query("Select * FROM " + MATERIACORRELATIVA + " WHERE idSub = ?", ROW_MAPPER_CORRELATIVAS, id );

        return list;
    }

    private List<Long> findProfessorsWithId(Long id){
        List<Long> list = jdbcTemplate.query("Select * FROM " + PROFESORMATERIA + " WHERE idSub = ?", ROW_MAPPER_PROFESORES, id );

        return list;
    }

    private List<Long> findCarrerasWithId(Long id){
        List<Long> list = jdbcTemplate.query("Select * FROM " + MATERIACARRERA + " WHERE idSub = ?", ROW_MAPPER_CARRERAS, id );

        return list;
    }
    @Override
    public Optional<Subject> findById(Long id) {
        Optional<Subject> resp = findByIdWithoutAll(id);
        if(!resp.isPresent())
            return Optional.empty();

        List<Long> correlativas = findCorrelativesWithId(id);

        List<Long> profesores = findProfessorsWithId(id);

        List<Long> carreras = findCarrerasWithId(id);

        Subject mat = new Subject(
                resp.get().getId(),
                resp.get().getName(),
                resp.get().getDepartment(),
                correlativas,
                profesores,
                carreras,
                resp.get().getCredits()
        );
        return Optional.of(mat);
    }
    @Override
    public List<Subject> getAll() {
        return jdbcTemplate.query("SELECT * FROM " + MATERIA, ROW_MAPPER);

    }
    @Override
    public List<Subject> getAllByCarrera(Long idCarrera){
        return null;
        //return jdbcTemplate.query("SELECT * FROM " + CARRERAMATERIA + "," + COURSE + "WHERE idMat=id and idCarr= ?", ROW_MAPPER, idCarrera);
    }

    @Override
    public void insert(Subject subject) {
        create(subject.getName(), subject.getDepartment(), subject.getPrerequisites(), subject.getProfessorIds(), subject.getDegreeIds(), subject.getCredits());
    }

    @Override
    public Subject create(String name, String depto, List<Long> idCorrelativas, List<Long> idProfesores, List<Long> idCarreras, int creditos){
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("depto", depto);
        data.put("idCorrelativas", idCorrelativas);
        data.put("idProfesores", idProfesores);
        data.put("idCarreras", idCarreras);
        data.put("creditos", creditos);

        Number key = jdbcInsert.executeAndReturnKey(data);

        return new Subject(key.longValue(), name, depto, idCorrelativas, idProfesores, idCarreras, creditos);
    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public void update(Subject subject) {

    }


}
