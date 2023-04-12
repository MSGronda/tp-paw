package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.security.cert.TrustAnchor;
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

    @Autowired
    public SubjectJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(TABLE_SUB)
                .usingGeneratedKeyColumns("id");
    }



    @Override
    public Optional<Subject> findById(String id) {
        Optional<Subject> resp = findByIdRaw(id);
        if(!resp.isPresent())
            return Optional.empty();

        List<String> prerequisites = findPrerequisites(id);
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
        List<Subject> resp = jdbcTemplate.query("SELECT * FROM " + TABLE_SUB, SubjectJdbcDao::rowMapperSubject);
        return fillSubjects(resp);
    }

    @Override
    public List<Subject> getByName(String name) {
        List<Subject> resp = jdbcTemplate.query("SELECT * FROM " + TABLE_SUB + " WHERE subname ILIKE ?",
                SubjectJdbcDao::rowMapperSubject, ("%" + name + "%"));

        return fillSubjects(resp);
    }


    // Toda las verificaciones de ob estan en la capa de servicio
    @Override
    public List<Subject> getByNameOrderedBy(String name, String ob) {
        List<Subject> resp = jdbcTemplate.query("SELECT * FROM " + TABLE_SUB + " WHERE subname ILIKE ? ORDER BY " + ob,
                SubjectJdbcDao::rowMapperSubject, ("%" + name + "%"));
        // TODO unificar las queries que se repiten

        return fillSubjects(resp);
    }


    @Override
    public List<Subject> getAllByDegree(Long idDegree) {
        List<Subject> resp = jdbcTemplate.query("SELECT * FROM " + TABLE_SUB_DEG + "," + TABLE_SUB + " WHERE idSub = id and idDeg = ?", SubjectJdbcDao::rowMapperSubject, idDegree);
        return fillSubjects(resp);
    }

    private List<Subject> fillSubjects(List<Subject> subjects){
        List<Subject> toRet = new ArrayList<>();

        subjects.forEach(subject -> {
            String id = subject.getId();

            List<String> prerequisites = findPrerequisites(id);
            List<Long> professors = findProfessors(id);
            List<Long> degrees = findDegrees(id);

            Subject subj = new Subject(
                    id,
                    subject.getName(),
                    subject.getDepartment(),
                    prerequisites,
                    professors,
                    degrees,
                    subject.getCredits()
            );
            toRet.add(subj);

        });
        return toRet;
    }

    @Override
    public void insert(Subject subject) {
        create(subject.getId(),subject.getName(), subject.getDepartment(), subject.getPrerequisites(), subject.getProfessorIds(), subject.getDegreeIds(), subject.getCredits());
    }

    @Override
    public Subject create(String id, String name, String depto, List<String> idCorrelativas, List<Long> idProfesores, List<Long> idCarreras, int creditos){

        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("department", depto);
        data.put("creditos", creditos);

        // TODO: Insert into corresponding tables
        // data.put("idCorrelativas", idCorrelativas);
        // data.put("idProfesores", idProfesores);
        // data.put("idCarreras", idCarreras);


       jdbcInsert.execute(data);

        return new Subject(id, name, depto, idCorrelativas, idProfesores, idCarreras, creditos);
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public void update(Subject subject) {

    }

    private static Subject rowMapperSubject(ResultSet rs, int rowNum) throws SQLException {
        return new Subject(
            rs.getString("id"),
            rs.getString("subName"),
            rs.getString("department"),
            rs.getInt("credits")
        );
    }

    //select * from professorSubject where idSub=?,id
    private static Long rowMapperProfessorId(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("idProf");
    }

    private static String rowMapperPrereqId(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("idPrereq");
    }

    private static Long rowMapperDegreeId(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("idDeg");
    }

    private static Long rowMapperSubDegSubId(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("idSub");
    }

    private Optional<Subject> findByIdRaw(String id){
        return jdbcTemplate.query("SELECT * FROM " + TABLE_SUB + " WHERE id = ?", SubjectJdbcDao::rowMapperSubject, id)
            .stream().findFirst();
    }

    private List<String> findPrerequisites(String id){
        return jdbcTemplate.query("Select * FROM " + TABLE_PREREQ + " WHERE idSub = ?", SubjectJdbcDao::rowMapperPrereqId, id );
    }

    private List<Long> findProfessors(String id){
        return jdbcTemplate.query("Select * FROM " + TABLE_PROF_SUB + " WHERE idSub = ?", SubjectJdbcDao::rowMapperProfessorId, id );
    }

    private List<Long> findDegrees(String id){
        return jdbcTemplate.query("Select * FROM " + TABLE_SUB_DEG + " WHERE idSub = ?", SubjectJdbcDao::rowMapperDegreeId, id );
    }
}
