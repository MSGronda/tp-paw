package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Professor;
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
public class SubjectClassJdbcDao implements SubjectClassDao{

    private static final String TABLE_CLASS = "class";
    private static final String TABLE_CLASS_LOC_TIME = "classLocTime";
    private static final String TABLE_CLASS_PROF = "classProfessors";
    private static final String TABLE_PROF = "professors";

    private static final String QUERY_JOIN = "SELECT * FROM " + TABLE_CLASS + " NATURAL JOIN " + TABLE_CLASS_LOC_TIME
            + " NATURAL JOIN " + TABLE_CLASS_PROF + " FULL JOIN " + TABLE_PROF + " ON " + TABLE_CLASS_PROF + ".idProf = " +
            TABLE_PROF + ".id";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertSubjectClass;
    private final SimpleJdbcInsert jdbcInsertSubjectClassLocTime;
    private final SimpleJdbcInsert jdbcInsertSubjectClassProfessor;

    @Autowired
    public SubjectClassJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);

        // TODO: check if this is correct
        this.jdbcInsertSubjectClass = new SimpleJdbcInsert(ds)
                .withTableName(TABLE_CLASS)
                .usingGeneratedKeyColumns("idSub","idClass");

        this.jdbcInsertSubjectClassLocTime = new SimpleJdbcInsert(ds)
                .withTableName(TABLE_CLASS_LOC_TIME)
                .usingGeneratedKeyColumns("idLocTime");

        jdbcInsertSubjectClassProfessor = new SimpleJdbcInsert(ds)
                .withTableName(TABLE_CLASS_PROF)
                .usingGeneratedKeyColumns("idSub","idClass","idProf");
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
