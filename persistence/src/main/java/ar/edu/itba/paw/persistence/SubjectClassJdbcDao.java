package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.SubjectClass;
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

    private static final String TABLE_CLASS = "class";
    private static final String TABLE_CLASS_LOC_TIME = "classLocTime";
    private static final String TABLE_CLASS_PROF = "classProfessors";
    private static final String TABLE_PROF = "professors";
    private static final String TABLE_SUBJECTS = "subjects";

    private static final String VIEW_JOIN = "joinedsubjects";
    private static final String USER_SUB_PRG_TABLE = "userSubjectProgress";

    private static final String QUERY_JOIN = "SELECT * FROM " + TABLE_CLASS + " NATURAL JOIN " + TABLE_CLASS_LOC_TIME
            + " NATURAL JOIN " + TABLE_CLASS_PROF + " FULL JOIN " + TABLE_PROF + " ON " + TABLE_CLASS_PROF + ".idProf = " +
            TABLE_PROF + ".id";

    private static final String COMPLETE_SUB =
            "SELECT *\n" +
            "FROM " + TABLE_SUBJECTS + " AS s LEFT JOIN " + TABLE_CLASS + " AS sc ON s.id = sc.idsub " + " LEFT JOIN " + TABLE_CLASS_LOC_TIME + " AS slt ON s.id = slt.idsub " +
            "WHERE s.id IN (SELECT v.id\n" +
            "                FROM " + VIEW_JOIN + " AS v\n" +
            "                WHERE v.id NOT IN (SELECT idSub FROM " + USER_SUB_PRG_TABLE + " WHERE idSub = v.id)\n" +
            "                GROUP BY v.id\n" +
            "                HAVING sum(CASE WHEN v.idprereq IS null THEN 1 ELSE 0 END) > 0\n" +
            "                    OR\n" +
            "                        COUNT(DISTINCT v.idprereq) =\n" +
            "                        (\n" +
            "                            SELECT COUNT(*)\n" +
            "                            FROM " + USER_SUB_PRG_TABLE  + " AS sp FULL JOIN prereqsubjects AS pr2 ON sp.idSub = pr2.idPreReq\n" +
            "                            WHERE sp.idUser = ? AND pr2.idSub = v.id\n" +
            "                            GROUP BY pr2.idSub\n" +
            "                        )\n" +
            "    )";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertSubjectClass;
    private final SimpleJdbcInsert jdbcInsertSubjectClassLocTime;
    private final SimpleJdbcInsert jdbcInsertSubjectClassProfessor;

    @Override
    public List<Subject> getAllSubsWithClassThatUserCanDo(long userId){
        return jdbcTemplate.query(COMPLETE_SUB, SubjectClassJdbcDao::multipleCompleteClassExtractor,userId);
    }

    private static List<Subject> multipleCompleteClassExtractor(ResultSet rs) throws SQLException {
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
                .withTableName(TABLE_CLASS)
                .usingGeneratedKeyColumns("idSub", "idClass");

        this.jdbcInsertSubjectClassLocTime = new SimpleJdbcInsert(ds)
                .withTableName(TABLE_CLASS_LOC_TIME)
                .usingGeneratedKeyColumns("idLocTime");

        jdbcInsertSubjectClassProfessor = new SimpleJdbcInsert(ds)
                .withTableName(TABLE_CLASS_PROF)
                .usingGeneratedKeyColumns("idSub", "idClass", "idProf");
    }

    @Override
    public List<SubjectClass> getBySubIdRaw(String idSub) {
        return jdbcTemplate.query("SELECT * FROM " + TABLE_CLASS + " WHERE idSub = ?", SubjectClassJdbcDao::rowMapperClass, idSub);
    }

    @Override
    public List<SubjectClass> getBySubId(String idSub) {
        return jdbcTemplate.query( QUERY_JOIN + " WHERE idSub = ?", SubjectClassJdbcDao::subjectListExtractor, idSub);
    }



    @Override
    public Optional<SubjectClass> findById(String s) {
        return Optional.empty();        // INVALID METHOD
    }

    @Override
    public List<SubjectClass> getAll() {
        return jdbcTemplate.query(QUERY_JOIN, SubjectClassJdbcDao::subjectListExtractor);
    }



    private static List<SubjectClass> subjectListExtractor(ResultSet rs) throws SQLException {
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



    private static SubjectClass rowMapperClass(ResultSet rs, int rowNum) throws SQLException {
        return new SubjectClass(
                rs.getString("idSub"),
                rs.getString("idClass")
        );
    }

    /* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @Override
    public void insert(SubjectClass subjectClass) {

    }

    @Override
    public void delete(String s) {

    }

    @Override
    public void update(SubjectClass subjectClass) {

    }
}
