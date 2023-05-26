//package ar.edu.itba.paw.persistence.dao.jdbc;
//
//import ar.edu.itba.paw.models.Professor;
//import ar.edu.itba.paw.persistence.constants.Tables;
//import ar.edu.itba.paw.persistence.dao.ProfessorDao;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
//import org.springframework.stereotype.Repository;
//
//import javax.sql.DataSource;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.*;
//
//@Repository
//public class ProfessorJdbcDao implements ProfessorDao {
//    private static final Logger LOGGER = LoggerFactory.getLogger(ProfessorJdbcDao.class);
//
//    private final JdbcTemplate jdbcTemplate;
//    private final SimpleJdbcInsert jdbcInsertProfesor;
//    private final SimpleJdbcInsert jdbcInsertMateriaProfesor;
//
//    @Autowired
//    public ProfessorJdbcDao(final DataSource ds) {
//        this.jdbcTemplate = new JdbcTemplate(ds);
//        this.jdbcInsertProfesor = new SimpleJdbcInsert(ds)
//                .withTableName(Tables.PROFS)
//                .usingGeneratedKeyColumns("id");
//
//        this.jdbcInsertMateriaProfesor = new SimpleJdbcInsert(ds)
//                .withTableName(Tables.PROFS_SUBJECTS)
//                .usingGeneratedKeyColumns("idProf", "idSub");
//    }
//
//    // Incluye una lista de todas las materias que esta enseñando el profesor.
//    @Override
//    public Optional<Professor> findById(final Long id) {
//        Optional<Professor> optProf = findByIdRaw(id);
//        if (!optProf.isPresent()) return Optional.empty();
//
//        Professor prof = optProf.get();
//
//        List<String> subjects = findSubjects(id);
//
//        Professor inflatedProf = new Professor(
//                prof.getId(),
//                prof.getName(),
//                subjects
//        );
//        return Optional.of(inflatedProf);
//    }
//
//    @Override
//    public void insert(final Professor professor) {
//        create(professor.getName(), professor.getSubjectIds());
//    }
//
//    @Override
//    public Professor create(final String name, final List<String> subjects) {
//        Map<String, Object> data = new HashMap<>();
//        data.put("profName", name);
//
//        Number key = jdbcInsertProfesor.executeAndReturnKey(data);
//
//        // Inserto en la tabla de Mat_Prof las materias que hace un profesor
//        for (String subject : subjects) {
//            Map<String, Object> matProf = new HashMap<>();
//            data.put("idProf", key);
//            data.put("idSub", subject);
//
//            jdbcInsertMateriaProfesor.executeAndReturnKey(matProf);
//        }
//        Professor professor = new Professor(key.longValue(), name, subjects);
//        LOGGER.info("Professor created with name {} in subjects", name);
//        return professor;
//    }
//
//    public List<Professor> getAll() {
//        return jdbcTemplate.query("SELECT * FROM " + Tables.PROFS, ProfessorJdbcDao::rowMapperProf);
//    }
//
//    @Override
//    public List<Professor> getAllBySubject(final String idSubject) {
//        return joinRowToProfs(jdbcTemplate.query("SELECT * FROM " + Tables.PROFS + " JOIN " + Tables.PROFS_SUBJECTS + " ps ON ps.idProf = id WHERE ps.idSub = ?", ProfessorJdbcDao::rowMapperJoin, idSubject));
//    }
//
//    @Override
//    public void delete(Long id) {
//
//    }
//
//    @Override
//    public void update(Professor professor) {
//
//    }
//    private static Professor rowMapperProf(ResultSet rs, int rowNum) throws SQLException {
//        return new Professor(
//                rs.getLong("id"),
//                rs.getString("profName")
//        );
//    }
//
//    private static String rowMapperSubId(final ResultSet rs, final int rowNum) throws SQLException {
//        return rs.getString("idSub");
//    }
//
//    private static JoinRow rowMapperJoin(final ResultSet rs, final int rowNum) throws SQLException {
//        return new JoinRow(
//                rs.getLong("idProf"),
//                rs.getString("profName"),
//                rs.getString("idSub")
//        );
//    }
//
//    private List<Professor> joinRowToProfs(final List<JoinRow> rows) {
//        Map<Long, Professor> profs = new HashMap<>();
//        for (JoinRow row : rows) {
//            final long idProf = row.getIdProf();
//            final String profName = row.getProfName();
//            final String idSub = row.getIdSub();
//
//            Professor prof = profs.getOrDefault(idProf, new Professor(idProf, profName));
//            prof.getSubjectIds().add(idSub);
//            profs.putIfAbsent(idProf, prof);
//        }
//        LOGGER.debug("Joined rows to professors");
//        return new ArrayList<>(profs.values());
//    }
//
//    // No incluye las materias que enseña, el atributo es null
//    private Optional<Professor> findByIdRaw(final Long id) {
//        return jdbcTemplate.query("SELECT * FROM " + Tables.PROFS + " WHERE id = ?", ProfessorJdbcDao::rowMapperProf, id).stream().findFirst();
//    }
//
//    // Dado un ID de prof, busco todos los ids de las materias en cual esta ese prof
//    private List<String> findSubjects(final Long id) {
//        return jdbcTemplate.query("SELECT * FROM " + Tables.PROFS_SUBJECTS + " WHERE idProf = ?", ProfessorJdbcDao::rowMapperSubId, id);
//    }
//
//
//    private static class JoinRow {
//        private final long idProf;
//        private final String profName;
//        private final String idSub;
//
//        public JoinRow(final long idProf, final String profName, final String idSub) {
//            this.idProf = idProf;
//            this.profName = profName;
//            this.idSub = idSub;
//        }
//
//        public long getIdProf() {
//            return idProf;
//        }
//
//        public String getProfName() {
//            return profName;
//        }
//
//        public String getIdSub() {
//            return idSub;
//        }
//    }
//}