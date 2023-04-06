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
    private static final String TABLE_SUB = "subjects";
    private static final String TABLE_PROF_SUB = "professorsSubjects";
    private static final String TABLE_PREREQ = "prereqSubjects";
    private static final String TABLE_SUB_DEG = "subjectsDegrees";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<Subject> ROW_MAPPER_SUBJECT = SubjectJdbcDao::rowMapperSubject;
    private static Subject rowMapperSubject(ResultSet rs, int rowNum) throws SQLException {
        return new Subject(
                rs.getLong("id"),
                rs.getString("subName"),
                rs.getString("department"),
                rs.getInt("credits")
        );
    }

    private static final RowMapper<Long> ROW_MAPPER_PROFESSOR_ID = SubjectJdbcDao::rowMapperProfessorId;

    //select * from professorSubject where idSub=?,id
    private static Long rowMapperProfessorId(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("idProf");
    }

    private static final RowMapper<Long> ROW_MAPPER_PREREQ_ID = SubjectJdbcDao::rowMapperPrereqId;
    private static Long rowMapperPrereqId(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("idCor");
    }

    private static final RowMapper<Long> ROW_MAPPER_DEGREE_ID = SubjectJdbcDao::rowMapperDegreeId;
    private static Long rowMapperDegreeId(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("idPrereq");
    }

    @Autowired
    public SubjectJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(TABLE_SUB)
                .usingGeneratedKeyColumns("id");
    }

    private Optional<Subject> findByIdRaw(Long id){
        return jdbcTemplate.query("SELECT * FROM " + TABLE_SUB + " WHERE id = ?", ROW_MAPPER_SUBJECT, id)
                .stream().findFirst();
    }

    private List<Long> findPrerequisites(Long id){
        return jdbcTemplate.query("Select * FROM " + TABLE_PREREQ + " WHERE idSub = ?", ROW_MAPPER_PREREQ_ID, id );
    }

    private List<Long> findProfessors(Long id){
        return jdbcTemplate.query("Select * FROM " + TABLE_PROF_SUB + " WHERE idSub = ?", ROW_MAPPER_PROFESSOR_ID, id );
    }

    private List<Long> findDegrees(Long id){
        return jdbcTemplate.query("Select * FROM " + TABLE_SUB_DEG + " WHERE idSub = ?", ROW_MAPPER_DEGREE_ID, id );
    }

    @Override
    public Optional<Subject> findById(Long id) {
        Optional<Subject> resp = findByIdRaw(id);
        if(!resp.isPresent())
            return Optional.empty();

        List<Long> prerequisites = findPrerequisites(id);
        List<Long> professors = findProfessors(id);
        List<Long> degrees = findDegrees(id);

        Subject subj = new Subject(
                resp.get().getId(),
                resp.get().getName(),
                resp.get().getDepartment(),
                prerequisites,
                professors,
                degrees,
                resp.get().getCredits()
        );
        return Optional.of(subj);
    }
    @Override
    public List<Subject> getAll() {
        return jdbcTemplate.query("SELECT * FROM " + TABLE_SUB, ROW_MAPPER_SUBJECT);

    }
    @Override
    public List<Subject> getAllByCarrera(Long idCarrera){
        return null;
        // TODO
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
        data.put("department", depto);
        data.put("creditos", creditos);

        // TODO: Insert into corresponding tables
        // data.put("idCorrelativas", idCorrelativas);
        // data.put("idProfesores", idProfesores);
        // data.put("idCarreras", idCarreras);


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
