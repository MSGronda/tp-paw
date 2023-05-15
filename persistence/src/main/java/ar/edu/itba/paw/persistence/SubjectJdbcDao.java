package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
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

import static ar.edu.itba.paw.persistence.Helpers.*;

@Repository
public class SubjectJdbcDao implements SubjectDao {
    private static final String VIEW_JOIN = "joinedsubjects";
    private static final String TABLE_SUB = "subjects";
    private static final String TABLE_PROF_SUB = "professorsSubjects";
    private static final String TABLE_PREREQ = "prereqSubjects";
    private static final String TABLE_SUB_DEG = "subjectsDegrees";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_USER_SUB_PROG = "usersubjectprogress";
    private static final String TABLE_REVIEWS = "reviews";

    private static final Map<String, String> queryOptionBlanck = new HashMap<>();

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private static final Logger LOGGER = LoggerFactory.getLogger(SubjectClassJdbcDao.class);
    private final String PAGE_SIZE = "12";

    @Autowired
    public SubjectJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(TABLE_SUB)
                .usingGeneratedKeyColumns("id");

        queryOptionBlanck.putIfAbsent("department","?");
        queryOptionBlanck.putIfAbsent("credits","CAST(? AS INTEGER)");
    }


    @Override
    public Optional<Subject> findById(final String id) {
        return jdbcTemplate.query("SELECT * FROM " + VIEW_JOIN + " WHERE id = ?", SubjectJdbcDao::subjectListExtractor, id)
                .stream().findFirst();
    }

    public List<Subject> findByIds(final List<String> ids) {
        if(ids.isEmpty()) return new ArrayList<>();

        return jdbcTemplate.query("SELECT * FROM " + VIEW_JOIN + " WHERE id IN (" + sqlPlaceholders(ids.size()) + ")",
                SubjectJdbcDao::subjectListExtractor,
                ids.toArray());
    }

    @Override
    public List<Subject> getAll() {
        return jdbcTemplate.query("SELECT * FROM " + VIEW_JOIN, SubjectJdbcDao::subjectListExtractor);
    }


    @Override
    public List<Subject> getByName(final String name) {
        List<Subject> toReturn = jdbcTemplate.query("SELECT * FROM " + VIEW_JOIN + " WHERE subname ILIKE ?",
                SubjectJdbcDao::subjectListExtractor, ("%" + name + "%"));
        LOGGER.info("Got subjects with name {}", name);
        return toReturn;
    }

    @Override
    public List<Subject> getByNameFiltered(final String name, final Map<String, String> filters) {
        // All filters in map must be valid. Checks are made in service.

        StringBuilder sb = new StringBuilder("SELECT * FROM ").append(TABLE_SUB).append(" WHERE subname ILIKE ?");
        List<String> filterList = new ArrayList<>();
        filterList.add("%" + name + "%");

        for (Map.Entry<String, String> filter : filters.entrySet()) {
            if(!Objects.equals(filter.getKey(), "ob") && !Objects.equals(filter.getKey(), "dir") && !Objects.equals(filter.getKey(),"pageNum")){
                sb.append(" AND ").append(filter.getKey()).append(" = ").append(queryOptionBlanck.get(filter.getKey()));
                filterList.add(filter.getValue());
            }
        }

        // Order by cannot use "?" in the SQL query
        sb.append(" ORDER BY ").append(filters.getOrDefault("ob","subname"));
        sb.append(" ").append(filters.getOrDefault("dir","ASC"));
        int offset = Integer.parseInt(filters.getOrDefault("pageNum","0")) * Integer.parseInt(PAGE_SIZE);
        sb.append(" LIMIT " + PAGE_SIZE + " OFFSET ").append(offset);
        List<Subject> toReturn = jdbcTemplate.query(sb.toString(), SubjectJdbcDao::subjectListExtractor,  filterList.toArray());
        LOGGER.info("Got subjects with name {} and filters {}", name, filters.values().stream().toString());
        return toReturn;
    }
    @Override
    public int getTotalPagesForSubjects(final String name, final Map<String, String> filters){
        StringBuilder sb = new StringBuilder("SELECT * FROM ").append(TABLE_SUB).append(" WHERE subname ILIKE ?");
        List<String> filterList = new ArrayList<>();
        filterList.add("%" + name + "%");

        for (Map.Entry<String, String> filter : filters.entrySet()) {
            if(!Objects.equals(filter.getKey(), "ob") && !Objects.equals(filter.getKey(), "dir") && !Objects.equals(filter.getKey(),"pageNum")){
                sb.append(" AND ").append(filter.getKey()).append(" = ").append(queryOptionBlanck.get(filter.getKey()));
                filterList.add(filter.getValue());
            }
        }

        // Order by cannot use "?" in the SQL query
        sb.append(" GROUP BY id,subname ");
        sb.append(" ORDER BY ").append(filters.getOrDefault("ob","subname"));
        sb.append(" ").append(filters.getOrDefault("dir","ASC"));
        List<Subject> toReturn = jdbcTemplate.query(sb.toString(), SubjectJdbcDao::subjectListExtractor,  filterList.toArray());
        LOGGER.info("Got subjects with name {} and filters {}", name, filters.values().stream().toString());
        return toReturn.size() / Integer.parseInt(PAGE_SIZE);
        //return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + TABLE_SUB + " WHERE subname ILIKE '%" + name + "%' ESCAPE '%'" ,Integer.class);
    }


    @Override
    public List<Subject> getAllByDegree(final Long idDegree) {
        return jdbcTemplate.query("SELECT * FROM " + VIEW_JOIN + " WHERE idDeg = ?", SubjectJdbcDao::subjectListExtractor, idDegree);
    }

    @Override
    public Map<Long, List<Subject>> getAllGroupedByDegreeId() {
        return groupByDegreeId(getAll());
    }

    private Map<Long, List<Subject>> groupByDegreeId(final List<Subject> subs) {
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

    @Override
    public Map<Long, Map<Integer, List<Subject>>> getAllGroupedByDegIdAndSemester() {
        return jdbcTemplate.query("SELECT * FROM " + VIEW_JOIN, SubjectJdbcDao::groupedByDegAndSemesterExtractor);
    }

    @Override
    public Map<Long, Map<Integer, List<Subject>>> getAllGroupedByDegIdAndYear(){
        Map<Long, Map<Integer, List<Subject>>> bySemester = getAllGroupedByDegIdAndSemester();
        Map<Long, Map<Integer, List<Subject>>> result = new LinkedHashMap<>();

        for(Map.Entry<Long, Map<Integer, List<Subject>>> degree : bySemester.entrySet()){
            Map<Integer, List<Subject>> byYearMap = new LinkedHashMap<>();
            for(Map.Entry<Integer, List<Subject>> semester : degree.getValue().entrySet()){
                if(semester.getKey() == -1) continue;

                int year = (int) Math.ceil(semester.getKey()/2.0d);

                List<Subject> subjects = byYearMap.getOrDefault(year, new ArrayList<>());
                subjects.addAll(semester.getValue());
                byYearMap.putIfAbsent(year, subjects);
            }
            result.putIfAbsent(degree.getKey(), byYearMap);
        }

        return result;
    }

    @Override
    public Map<Long, List<Subject>> getAllElectivesGroupedByDegId(){
        Map<Long, List<Subject>> result = new LinkedHashMap<>();
        Map<Long, Map<Integer, List<Subject>>> bySemester = getAllGroupedByDegIdAndSemester();

        for(Map.Entry<Long, Map<Integer, List<Subject>>> degree : bySemester.entrySet()){
            result.put(degree.getKey(), degree.getValue().getOrDefault(-1, new ArrayList<>()));
        }

        return result;
    }

    @Override
    public void insert(final Subject subject) {
        create(subject.getId(), subject.getName(), subject.getDepartment(), subject.getPrerequisites(), subject.getProfessorIds(), subject.getDegreeIds(), subject.getCredits());
    }

    @Override
    public Subject create(final String id, final String name, final String depto, final Set<String> idCorrelativas,
                          final Set<Long> idProfesores, final Set<Long> idCarreras, final int creditos) {

        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("department", depto);
        data.put("creditos", creditos);

        // TODO: Insert into corresponding tables
        // data.put("idCorrelativas", idCorrelativas);
        // data.put("idProfesores", idProfesores);
        // data.put("idCarreras", idCarreras);


        jdbcInsert.execute(data);
        LOGGER.info("Created new subject with id {} and name {} from {} department",id, name, depto);
        return new Subject(id, name, depto, idCorrelativas, idProfesores, idCarreras, creditos);
    }

    @Override
    public void delete(final String id) {

    }

    public Map<User,Set<Subject>> getAllUserUnreviewedNotifSubjects() {
        return jdbcTemplate.query(
                "SELECT * FROM " + TABLE_USERS + " u" +
                        " LEFT JOIN " + TABLE_USER_SUB_PROG + " usp ON u.id = usp.iduser" +
                        " LEFT JOIN " + TABLE_SUB + " s ON usp.idsub = s.id" +
                        " WHERE usp.subjectstate <> 0" +
                        " AND (usp.notiftime IS NULL OR usp.notiftime < now() - interval '1 week')" +
                        " AND u.id NOT IN (SELECT iduser FROM " + TABLE_REVIEWS + " r WHERE r.idsub = usp.idsub)",

                SubjectJdbcDao::userUnreviewedNotifSubjectExtractor
        );
    }

    @Override
    public void updateUnreviewedNotifTime() {
        jdbcTemplate.update(
                "UPDATE " + TABLE_USER_SUB_PROG + " usp SET notiftime = now()" +
                        " WHERE usp.subjectstate <> 0" +
                        " AND usp.iduser NOT IN (SELECT iduser FROM " + TABLE_REVIEWS + " r WHERE r.idsub = usp.idsub)"
        );
    }

    @Override
    public void update(final Subject subject) {

    }

    private List<String> findPrerequisites(final String id){
        return jdbcTemplate.query("Select * FROM " + TABLE_PREREQ + " WHERE idSub = ?", SubjectJdbcDao::rowMapperPrereqId, id );
    }

    private List<Long> findProfessors(final String id){
        return jdbcTemplate.query("Select * FROM " + TABLE_PROF_SUB + " WHERE idSub = ?", SubjectJdbcDao::rowMapperProfessorId, id );
    }

    private List<Long> findDegrees(final String id){
        return jdbcTemplate.query("Select * FROM " + TABLE_SUB_DEG + " WHERE idSub = ?", SubjectJdbcDao::rowMapperDegreeId, id );
    }

    private static Long rowMapperProfessorId(final ResultSet rs, final int rowNum) throws SQLException {
        return rs.getLong("idProf");
    }

    private static String rowMapperPrereqId(final ResultSet rs, final int rowNum) throws SQLException {
        return rs.getString("idPrereq");
    }

    private static Long rowMapperDegreeId(final ResultSet rs, final int rowNum) throws SQLException {
        return rs.getLong("idDeg");
    }

    private static List<Subject> subjectListExtractor(final ResultSet rs) throws SQLException {
        final Map<String, Subject> subs = new LinkedHashMap<>();

        while (rs.next()) {
            final String idSub = rs.getString("id");
            final String subName = rs.getString("subname");
            final String department = rs.getString("department");
            final int credits = rs.getInt("credits");
            final Optional<String> idPrereq = Optional.ofNullable(rs.getString("idprereq"));
            final Optional<Long> idProf = getOptionalLong(rs, "idprof");
            final Optional<Long> idDeg = getOptionalLong(rs, "iddeg");

            final Subject sub = subs.getOrDefault(idSub,
                    new Subject(idSub, subName, department, credits)
            );

            idPrereq.ifPresent(id -> sub.getPrerequisites().add(id));
            idProf.ifPresent(id -> sub.getProfessorIds().add(id));
            idDeg.ifPresent(id -> sub.getDegreeIds().add(id));

            subs.putIfAbsent(idSub, sub);
        }

        return new ArrayList<>(subs.values());
    }

    private static Map<Long, Map<Integer,List<Subject>>> groupedByDegAndSemesterExtractor(final ResultSet rs) throws SQLException {
        final Map<Long, Map<Integer, Map<String,Subject>>> auxMap = new LinkedHashMap<>();

        while (rs.next()) {
            final String idSub = rs.getString("id");
            final String subName = rs.getString("subname");
            final String department = rs.getString("department");
            final int credits = rs.getInt("credits");
            final Optional<String> idPrereq = Optional.ofNullable(rs.getString("idprereq"));
            final Optional<Long> idProf = getOptionalLong(rs, "idprof");
            final Optional<Long> idDeg = getOptionalLong(rs, "iddeg");
            final Optional<Integer> semester = getOptionalInt(rs, "semester");

            if(!idDeg.isPresent() || !semester.isPresent()) continue;

            final Map<Integer, Map<String,Subject>> semesterMap = auxMap.getOrDefault(idDeg.get(), new LinkedHashMap<>());
            final Map<String,Subject> subs = semesterMap.getOrDefault(semester.get(), new LinkedHashMap<>());
            final Subject sub = subs.getOrDefault(idSub,
                    new Subject(idSub, subName, department, credits)
            );

            sub.getDegreeIds().add(idDeg.get());
            idPrereq.ifPresent(id -> sub.getPrerequisites().add(id));
            idProf.ifPresent(id -> sub.getProfessorIds().add(id));

            subs.putIfAbsent(idSub, sub);
            semesterMap.putIfAbsent(semester.get(), subs);
            auxMap.putIfAbsent(idDeg.get(), semesterMap);
        }

        final Map<Long, Map<Integer, List<Subject>>> result = new LinkedHashMap<>();
        for(Map.Entry<Long,Map<Integer,Map<String,Subject>>> degMap : auxMap.entrySet()) {
            final Map<Integer,List<Subject>> newSemesterMap = new LinkedHashMap<>();
            for(Map.Entry<Integer,Map<String,Subject>> semesterMap : degMap.getValue().entrySet()) {
                newSemesterMap.put(semesterMap.getKey(), new ArrayList<>(semesterMap.getValue().values()));
            }
            result.put(degMap.getKey(), newSemesterMap);
        }

        return result;
    }

    private static Map<User,Set<Subject>> userUnreviewedNotifSubjectExtractor(ResultSet rs) throws SQLException {
        final Map<User,Set<Subject>> map = new LinkedHashMap<>();

        while(rs.next()) {
            final long idUser = rs.getLong("iduser");
            final String username = rs.getString("username");
            final String email = rs.getString("email");
            final String idSub = rs.getString("idsub");
            final String subName = rs.getString("subname");
            final String department = rs.getString("department");
            final int credits = rs.getInt("credits");

            final User user = new User.UserBuilder(email, "", username)
                    .id(idUser)
                    .build();
            final Subject sub = new Subject(idSub, subName, department, credits);

            final Set<Subject> subs = map.getOrDefault(user, new LinkedHashSet<>());
            subs.add(sub);
            map.putIfAbsent(user, subs);
        }

        return map;
    }
}
