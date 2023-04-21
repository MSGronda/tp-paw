package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.swing.text.html.Option;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class SubjectJdbcDao implements SubjectDao {
    private static final String TABLE_SUB = "subjects";
    private static final String TABLE_PROF_SUB = "professorsSubjects";
    private static final String TABLE_PREREQ = "prereqSubjects";
    private static final String TABLE_SUB_DEG = "subjectsDegrees";

    private static final String QUERY_JOIN = "SELECT * FROM " + TABLE_SUB + " s\n" +
            "FULL JOIN " + TABLE_PREREQ + " prereq ON s.id = prereq.idsub\n" +
            "FULL JOIN " + TABLE_PROF_SUB + " prof ON s.id = prof.idsub\n" +
            "FULL JOIN " + TABLE_SUB_DEG + " deg ON s.id = deg.idsub";

    private static final List<String> validFilters = Arrays.asList("department", "credits");
    private static final List<String> validOrderBy = Arrays.asList("id", "credits", "subname");


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
    public Map<String, String> findPrerequisitesName(String id) {
        List<String> prerequisites = findPrerequisites(id);

        Map<String, String> prereqNames = new HashMap<>();

        for (String prerequisite : prerequisites) {
            Optional<Subject> maybeSubject = findById(prerequisite);
            maybeSubject.ifPresent(subject -> prereqNames.put(subject.getId(), subject.getName()));
        }

        return prereqNames;
    }

    @Override
    public Optional<Subject> findById(String id) {
        return joinRowsToSubjects(jdbcTemplate.query(QUERY_JOIN + " WHERE id = ?", SubjectJdbcDao::rowMapperJoinRow, id))
                .stream().findFirst();
    }

    @Override
    public List<Subject> getAll() {
        return joinRowsToSubjects(jdbcTemplate.query(QUERY_JOIN, SubjectJdbcDao::rowMapperJoinRow));
    }


    // TODO unificar las queries que se repiten
    @Override
    public List<Subject> getByName(String name) {
        return joinRowsToSubjects(jdbcTemplate.query(QUERY_JOIN + " WHERE subname ILIKE ?",
                SubjectJdbcDao::rowMapperJoinRow, ("%" + name + "%")));
    }

    // TODO unificar las queries que se repiten
    @Override
    public List<Subject> getByNameOrderedBy(String name, String ob) {
        return joinRowsToSubjects(jdbcTemplate.query(QUERY_JOIN + " WHERE subname ILIKE ? ORDER BY " + ob,
                SubjectJdbcDao::rowMapperJoinRow, ("%" + name + "%")));
    }

    @Override
    public List<Subject> getByNameFiltered(String name, Map<String, String> filters, String ob) {
        StringBuilder sb = new StringBuilder(QUERY_JOIN).append(" WHERE subname ILIKE ?");

        // Me fijo que el filtro que me estan pasando sea valido
        List<Map.Entry<String, String>> validArgValuePair = filters.entrySet().stream()
                .filter(entry -> validFilters.contains(entry.getKey())).collect(Collectors.toList());

        // Agrego los filtros validos
        for (Map.Entry<String, String> filter : validArgValuePair) {
            // TODO: this is unsafe, change!
            sb.append(" AND ").append(filter.getKey()).append(" = ").append("'").append(filter.getValue()).append("'");
        }

        // Me fijo que el order by sea valido
        if (!validOrderBy.contains(ob))
            ob = "subname";      // Default
        sb.append(" ORDER BY ").append(ob);

        return joinRowsToSubjects(jdbcTemplate.query(sb.toString(), SubjectJdbcDao::rowMapperJoinRow, "%" + name + "%"));
    }


    @Override
    public List<Subject> getAllByDegree(Long idDegree) {
        return joinRowsToSubjects(jdbcTemplate.query(QUERY_JOIN + " WHERE idDeg = ?", SubjectJdbcDao::rowMapperJoinRow, idDegree));
    }

    @Override
    public Map<Long, List<Subject>> getAllGroupedByDegreeId() {
        return groupByDegreeId(getAll());
    }

    @Override
    public Map<Long, Map<Integer, List<Subject>>> getAllGroupedByDegIdAndSemester() {
        return groupByDegIdAndSemester(jdbcTemplate.query(QUERY_JOIN, SubjectJdbcDao::rowMapperJoinRow));
    }

    @Override
    public Map<Long, Map<Integer, List<Subject>>> getAllGroupedByDegIdAndYear(){
        return groupByDegIdAndYear(jdbcTemplate.query(QUERY_JOIN, SubjectJdbcDao::rowMapperJoinRow));
    }

    @Override
    public Map<Long, List<Subject>> getAllElectivesGroupedByDegId(){
        return electivesGroupedByDegId(jdbcTemplate.query(QUERY_JOIN, SubjectJdbcDao::rowMapperJoinRow));
    }

    private Map<Long, List<Subject>> groupByDegreeId(List<Subject> subs) {
        Map<Long, List<Subject>> map = new HashMap<>();
        for (Subject sub : subs) {
            for (Long id : sub.getDegreeIds()) {
                List<Subject> degSubs = map.getOrDefault(id, new ArrayList<>());
                degSubs.add(sub);
                map.putIfAbsent(id, degSubs);
            }
        }
        return map;
    }

    private Map<Long, Map<Integer,List<Subject>>> groupByDegIdAndSemester(List<JoinRow> rows) {
        Map<Long, Map<Integer, Map<String,Subject>>> auxMap = new LinkedHashMap<>();

        for (JoinRow row : rows) {
            if(!row.idDeg.isPresent() || !row.semester.isPresent()) continue;
            long idDeg = row.idDeg.get();
            int semester = row.semester.get();

            Map<Integer, Map<String, Subject>> semesterMap = auxMap.getOrDefault(idDeg, new LinkedHashMap<>());
            Map<String,Subject> subs = semesterMap.getOrDefault(semester, new LinkedHashMap<>());
            Subject sub = subs.getOrDefault(row.idSub, new Subject(row.idSub, row.subName, row.department, row.credits));

            row.idProf.ifPresent(id -> sub.getProfessorIds().add(id));
            row.idPrereq.ifPresent(id -> sub.getPrerequisites().add(id));
            row.idDeg.ifPresent(id -> sub.getDegreeIds().add(id));

            subs.putIfAbsent(row.idSub, sub);
            semesterMap.putIfAbsent(semester, subs);
            auxMap.putIfAbsent(idDeg, semesterMap);
        }

        Map<Long, Map<Integer, List<Subject>>> result = new LinkedHashMap<>();
        for(Map.Entry<Long,Map<Integer,Map<String,Subject>>> degMap : auxMap.entrySet()) {
            Map<Integer,List<Subject>> newSemesterMap = new LinkedHashMap<>();
            for(Map.Entry<Integer,Map<String,Subject>> semesterMap : degMap.getValue().entrySet()) {
                newSemesterMap.put(semesterMap.getKey(), new ArrayList<>(semesterMap.getValue().values()));
            }
            result.put(degMap.getKey(), newSemesterMap);
        }

        return result;
    }

    private Map<Long, Map<Integer, List<Subject>>> groupByDegIdAndYear(List<JoinRow> rows){
        Map<Long, Map<Integer, Map<String,Subject>>> auxMap = new LinkedHashMap<>();

        for (JoinRow row : rows) {
            if (!row.idDeg.isPresent() || !row.semester.isPresent() || row.semester.get() == -1) continue;
            long idDeg = row.idDeg.get();
            int semester = row.semester.get();

            Map<Integer, Map<String, Subject>> yearMap = auxMap.getOrDefault(idDeg, new LinkedHashMap<>());

            if (semester % 2 == 1) {
                Map<String, Subject> subs = yearMap.getOrDefault((semester + 1) / 2, new LinkedHashMap<>());

                Subject sub = subs.getOrDefault(row.idSub, new Subject(row.idSub, row.subName, row.department, row.credits));

                row.idProf.ifPresent(id -> sub.getProfessorIds().add(id));
                row.idPrereq.ifPresent(id -> sub.getPrerequisites().add(id));
                row.idDeg.ifPresent(id -> sub.getDegreeIds().add(id));

                subs.putIfAbsent(row.idSub, sub);
                if (yearMap.containsKey((semester + 1) / 2)) {
                    yearMap.get((semester + 1) / 2).putAll(subs);
                } else {
                    yearMap.putIfAbsent((semester + 1) / 2, subs);
                }

                auxMap.putIfAbsent(idDeg, yearMap);

            } else {
                Map<String, Subject> subs = yearMap.getOrDefault(semester / 2, new LinkedHashMap<>());

                Subject sub = subs.getOrDefault(row.idSub, new Subject(row.idSub, row.subName, row.department, row.credits));

                row.idProf.ifPresent(id -> sub.getProfessorIds().add(id));
                row.idPrereq.ifPresent(id -> sub.getPrerequisites().add(id));
                row.idDeg.ifPresent(id -> sub.getDegreeIds().add(id));

                subs.putIfAbsent(row.idSub, sub);
                if (yearMap.containsKey(semester / 2)) {
                    yearMap.get(semester / 2).putAll(subs);
                } else {
                    yearMap.putIfAbsent(semester / 2, subs);
                }
                auxMap.putIfAbsent(idDeg, yearMap);
            }
        }

        Map<Long, Map<Integer, List<Subject>>> result = new LinkedHashMap<>();
        for(Map.Entry<Long,Map<Integer,Map<String,Subject>>> degMap : auxMap.entrySet()) {
            Map<Integer,List<Subject>> newYearMap = new LinkedHashMap<>();
            for(Map.Entry<Integer,Map<String,Subject>> yearMap : degMap.getValue().entrySet()) {
                newYearMap.put(yearMap.getKey(), new ArrayList<>(yearMap.getValue().values()));
            }
            result.put(degMap.getKey(), newYearMap);
        }

        return result;
    }

    private Map<Long, List<Subject>> electivesGroupedByDegId(List<JoinRow> rows){
        Map<Long, Map<String,Subject>> auxMap = new LinkedHashMap<>();

        for (JoinRow row : rows) {
            if (!row.idDeg.isPresent() || !row.semester.isPresent() || row.semester.get() != -1) continue;
            long idDeg = row.idDeg.get();
            int semester = row.semester.get();

            Map<String, Subject> subs = auxMap.getOrDefault(idDeg, new LinkedHashMap<>());

            Subject sub = subs.getOrDefault(row.idSub, new Subject(row.idSub, row.subName, row.department, row.credits));

            row.idProf.ifPresent(id -> sub.getProfessorIds().add(id));
            row.idPrereq.ifPresent(id -> sub.getPrerequisites().add(id));
            row.idDeg.ifPresent(id -> sub.getDegreeIds().add(id));

            subs.putIfAbsent(row.idSub, sub);

            auxMap.putIfAbsent(idDeg, subs);

        }

        Map<Long, List<Subject>> result = new LinkedHashMap<>();
        for(Map.Entry<Long,Map<String,Subject>> degMap : auxMap.entrySet()) {
            List<Subject> newElectiveList = new ArrayList<>();
            for(Map.Entry<String, Subject> electiveMap : degMap.getValue().entrySet()) {
                newElectiveList.add(electiveMap.getValue());
            }
            result.put(degMap.getKey(), newElectiveList);
        }

        return result;
    }

    @Override
    public void insert(Subject subject) {
        create(subject.getId(), subject.getName(), subject.getDepartment(), subject.getPrerequisites(), subject.getProfessorIds(), subject.getDegreeIds(), subject.getCredits());
    }

    @Override
    public Subject create(String id, String name, String depto, Set<String> idCorrelativas, Set<Long> idProfesores, Set<Long> idCarreras, int creditos) {

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

    private List<String> findPrerequisites(String id){
        return jdbcTemplate.query("Select * FROM " + TABLE_PREREQ + " WHERE idSub = ?", SubjectJdbcDao::rowMapperPrereqId, id );
    }

    private List<Long> findProfessors(String id){
        return jdbcTemplate.query("Select * FROM " + TABLE_PROF_SUB + " WHERE idSub = ?", SubjectJdbcDao::rowMapperProfessorId, id );
    }

    private List<Long> findDegrees(String id){
        return jdbcTemplate.query("Select * FROM " + TABLE_SUB_DEG + " WHERE idSub = ?", SubjectJdbcDao::rowMapperDegreeId, id );
    }

    private static Long rowMapperProfessorId(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("idProf");
    }

    private static String rowMapperPrereqId(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("idPrereq");
    }

    private static Long rowMapperDegreeId(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("idDeg");
    }

    private static List<Subject> joinRowsToSubjects(List<JoinRow> rows) {
        final Map<String, Subject> subs = new HashMap<>();
        for (JoinRow row : rows) {
            Subject sub = subs.getOrDefault(row.idSub,
                    new Subject(row.idSub, row.subName, row.department, row.credits)
            );

            row.idPrereq.ifPresent(idPrereq -> sub.getPrerequisites().add(idPrereq));
            row.idProf.ifPresent(id -> sub.getProfessorIds().add(id));
            row.idDeg.ifPresent(idDeg -> sub.getDegreeIds().add(idDeg));

            subs.putIfAbsent(row.idSub, sub);
        }

        return new ArrayList<>(subs.values());
    }

    private static JoinRow rowMapperJoinRow(ResultSet rs, int rowNum) throws SQLException {
        return new JoinRow(
                rs.getString("id"),
                rs.getString("subName"),
                rs.getString("department"),
                rs.getInt("credits"),
                Optional.ofNullable(rs.getString("idprereq")),
                Optional.of(rs.getLong("idprof")),
                Optional.of(rs.getLong("iddeg")),
                Optional.of(rs.getInt("semester"))
        );
    }

    private static class JoinRow {
        private final String idSub;
        private final String subName;
        private final String department;
        private final int credits;
        private final Optional<String> idPrereq;
        private final Optional<Long> idProf;
        private final Optional<Long> idDeg;
        private final Optional<Integer> semester;

        public JoinRow(
                String idSub,
                String subName,
                String department,
                int credits,
                Optional<String> idPrereq,
                Optional<Long> idProf,
                Optional<Long> idDeg,
                Optional<Integer> semester
        ) {
            this.idSub = idSub;
            this.subName = subName;
            this.department = department;
            this.credits = credits;
            this.idPrereq = idPrereq;
            this.idProf = idProf;
            this.idDeg = idDeg;
            this.semester = semester;
        }
    }
}
