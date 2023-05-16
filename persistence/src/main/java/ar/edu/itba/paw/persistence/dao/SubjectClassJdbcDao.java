package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.SubjectClass;
import ar.edu.itba.paw.persistence.constants.Tables;
import ar.edu.itba.paw.persistence.constants.Views;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.*;

@Repository
public class SubjectClassJdbcDao implements SubjectClassDao {
    private static final String QUERY_JOIN = "SELECT * FROM " + Tables.CLASS + " NATURAL JOIN " + Tables.CLASS_LOCTIME
            + " NATURAL JOIN " + Tables.CLASS_PROFS + " FULL JOIN " + Tables.PROFS + " ON " + Tables.CLASS_PROFS + ".idProf = " +
        Tables.PROFS + ".id";

    private static final String COMPLETE_SUB =
            "SELECT *\n" +
            "FROM " + Tables.SUBJECTS + " AS s LEFT JOIN " + Tables.CLASS + " AS sc ON s.id = sc.idsub " + " LEFT JOIN " + Tables.CLASS_LOCTIME + " AS slt ON s.id = slt.idsub AND slt.idclass = sc.idclass " +
            "WHERE s.id IN (SELECT v.id\n" +
            "                FROM " + Views.JOINED_SUBJECTS + " AS v\n" +
            "                WHERE v.id NOT IN (SELECT idSub FROM " + Tables.USER_SUBJECT_PROGRESS + " WHERE idSub = v.id)\n" +
            "                GROUP BY v.id\n" +
            "                HAVING sum(CASE WHEN v.idprereq IS null THEN 1 ELSE 0 END) > 0\n" +
            "                    OR\n" +
            "                        COUNT(DISTINCT v.idprereq) =\n" +
            "                        (\n" +
            "                            SELECT COUNT(*)\n" +
            "                            FROM " + Tables.USER_SUBJECT_PROGRESS  + " AS sp FULL JOIN prereqsubjects AS pr2 ON sp.idSub = pr2.idPreReq\n" +
            "                            WHERE sp.idUser = ? AND pr2.idSub = v.id\n" +
            "                            GROUP BY pr2.idSub\n" +
            "                        )\n" +
            "    )";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertSubjectClass;
    private final SimpleJdbcInsert jdbcInsertSubjectClassLocTime;
    private final SimpleJdbcInsert jdbcInsertSubjectClassProfessor;

    @Override
    public List<Subject> getAllSubsWithClassThatUserCanDo(final long userId){
        return jdbcTemplate.query(COMPLETE_SUB, SubjectClassJdbcDao::multipleCompleteClassExtractor,userId);
    }

    private static List<Subject> multipleCompleteClassExtractor(final ResultSet rs) throws SQLException {
        Map<String,Subject> classes = new HashMap<>();

        while (rs.next()) {

            String idSub = rs.getString("id");
            String subName = rs.getString("subname");
            String department = rs.getString("department");
            int credits = rs.getInt("credits");

            Subject sub = classes.getOrDefault(idSub, new Subject(idSub,subName,department,credits));

            String idClass = rs.getString("idClass");
            Integer day = rs.getInt("day");
            Time start = rs.getTime("startTime");
            Time end = rs.getTime("endTime");
            String classNumber = rs.getString("class");
            String building = rs.getString("building");
            String mode = rs.getString("mode");

            SubjectClass subjectClass = sub.getSubjectClasses().getOrDefault(idSub+idClass, new SubjectClass(idSub,idClass));
            subjectClass.getClassTimes().add(new SubjectClass.ClassTime(day,start,end,classNumber,building,mode));

            sub.getSubjectClasses().put(idSub+idClass, subjectClass);
            classes.put(idSub,sub);
        }

        return new ArrayList<>(classes.values());
    }


    @Autowired
    public SubjectClassJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);

        // TODO: check if this is correct
        this.jdbcInsertSubjectClass = new SimpleJdbcInsert(ds)
                .withTableName(Tables.CLASS)
                .usingGeneratedKeyColumns("idSub", "idClass");

        this.jdbcInsertSubjectClassLocTime = new SimpleJdbcInsert(ds)
                .withTableName(Tables.CLASS_LOCTIME)
                .usingGeneratedKeyColumns("idLocTime");

        jdbcInsertSubjectClassProfessor = new SimpleJdbcInsert(ds)
                .withTableName(Tables.CLASS_PROFS)
                .usingGeneratedKeyColumns("idSub", "idClass", "idProf");
    }

    @Override
    public List<SubjectClass> getBySubIdRaw(final String idSub) {
        return jdbcTemplate.query("SELECT * FROM " + Tables.CLASS + " WHERE idSub = ?", SubjectClassJdbcDao::rowMapperClass, idSub);
    }

    @Override
    public List<SubjectClass> getBySubId(final String idSub) {
        return jdbcTemplate.query( QUERY_JOIN + " WHERE idSub = ?", SubjectClassJdbcDao::subjectListExtractor, idSub);
    }



    @Override
    public Optional<SubjectClass> findById(final String s) {
        return Optional.empty();        // INVALID METHOD
    }

    @Override
    public List<SubjectClass> getAll() {
        return jdbcTemplate.query(QUERY_JOIN, SubjectClassJdbcDao::subjectListExtractor);
    }



    private static List<SubjectClass> subjectListExtractor(final ResultSet rs) throws SQLException {
        final Map<String, SubjectClass> subClasses = new HashMap<>();
        while (rs.next()) {
            String idSub = rs.getString("idSub");
            String idClass = rs.getString("idClass");
            Integer day = rs.getInt("day");
            Time start = rs.getTime("startTime");
            Time end = rs.getTime("endTime");
            String classNumber = rs.getString("class");
            String building = rs.getString("building");
            String mode = rs.getString("mode");
            String profName = rs.getString("profName");
            int profId = rs.getInt("id");

            final SubjectClass subClass = subClasses.getOrDefault(idSub+idClass, new SubjectClass(idSub, idClass));

            subClass.getClassTimes().add(new SubjectClass.ClassTime(day, start, end, classNumber, building, mode));
            subClass.getProfessors().add(new Professor(profId, profName));
            subClasses.put(idSub+idClass, subClass);
        }

        return new ArrayList<>(subClasses.values());
    }



    private static SubjectClass rowMapperClass(final ResultSet rs, int rowNum) throws SQLException {
        return new SubjectClass(
                rs.getString("idSub"),
                rs.getString("idClass")
        );
    }

    /* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @Override
    public void insert(final SubjectClass subjectClass) {

    }

    @Override
    public void delete(final String s) {

    }

    @Override
    public void update(final SubjectClass subjectClass) {

    }
}
