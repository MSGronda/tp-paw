package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Materia;
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
public class MateriaJdbcDao implements MateriaDao {

    private static final RowMapper<Materia> ROW_MAPPER = MateriaJdbcDao::rowMapper;
    private static Materia rowMapper(ResultSet rs, int rowNum) throws SQLException {
        return new Materia(
                rs.getLong("id"),
                rs.getString("nombre"),
                rs.getString("depto"),
//                new ArrayList<>(), //TODO - query para lista de id de correlativas
//                new ArrayList<>(),  //TODO - query para lista de id de profesores
//                new ArrayList<>(),   //TODO - query para lista de id de carreras
                rs.getInt("creditos")
        );
    }

    private static final RowMapper<Long> ROW_MAPPER_PROFESORES = MateriaJdbcDao::rowMapperProfesores;

    //select * from professorSubject where idSub=?,id
    private static Long rowMapperProfesores(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("idProf");
    }

    private static final RowMapper<Long> ROW_MAPPER_CORRELATIVAS = MateriaJdbcDao::rowMapperCorrelativas;
    private static Long rowMapperCorrelativas(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("idCor");
    }

    private static final RowMapper<Long> ROW_MAPPER_CARRERAS = MateriaJdbcDao::rowMapperCarreras;
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
    public MateriaJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(MATERIA)
                .usingGeneratedKeyColumns("id");
    }

    private Optional<Materia> findByIdWithoutAll(Long id){
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
    public Optional<Materia> findById(Long id) {
        Optional<Materia> resp = findByIdWithoutAll(id);
        if(!resp.isPresent())
            return Optional.empty();

        List<Long> correlativas = findCorrelativesWithId(id);

        List<Long> profesores = findProfessorsWithId(id);

        List<Long> carreras = findCarrerasWithId(id);

        Materia mat = new Materia(
                resp.get().getId(),
                resp.get().getNombre(),
                resp.get().getDepto(),
                correlativas,
                profesores,
                carreras,
                resp.get().getCreditos()
        );
        return Optional.of(mat);
    }
    @Override
    public List<Materia> getAll() {
        return jdbcTemplate.query("SELECT * FROM " + MATERIA, ROW_MAPPER);

    }
    @Override
    public List<Materia> getAllByCarrera(Long idCarrera){
        return null;
        //return jdbcTemplate.query("SELECT * FROM " + CARRERAMATERIA + "," + COURSE + "WHERE idMat=id and idCarr= ?", ROW_MAPPER, idCarrera);
    }

    @Override
    public void insert(Materia materia) {
        create(materia.getNombre(), materia.getDepto(), materia.getCorrelativas(), materia.getIdProfesores(), materia.getIdCarreras(), materia.getCreditos());
    }

    @Override
    public Materia create(String name, String depto, List<Long> idCorrelativas, List<Long> idProfesores, List<Long> idCarreras, int creditos){
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("depto", depto);
        data.put("idCorrelativas", idCorrelativas);
        data.put("idProfesores", idProfesores);
        data.put("idCarreras", idCarreras);
        data.put("creditos", creditos);

        Number key = jdbcInsert.executeAndReturnKey(data);

        return new Materia(key.longValue(), name, depto, idCorrelativas, idProfesores, idCarreras, creditos);
    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public void update(Materia materia) {

    }


}
