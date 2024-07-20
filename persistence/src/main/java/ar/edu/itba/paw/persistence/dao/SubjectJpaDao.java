package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.OrderDir;
import ar.edu.itba.paw.models.enums.SubjectFilterField;
import ar.edu.itba.paw.models.enums.SubjectOrderField;
import ar.edu.itba.paw.models.exceptions.SubjectClassIdAlreadyExistsException;
import ar.edu.itba.paw.models.exceptions.SubjectIdAlreadyExistsException;
import ar.edu.itba.paw.models.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.models.utils.SubjectSearchParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class SubjectJpaDao implements SubjectDao {
    private final static Logger LOGGER = LoggerFactory.getLogger(SubjectJpaDao.class);
    private static final int PAGE_SIZE = 20;
    @PersistenceContext
    private EntityManager em;

    @Override
    public Subject create(final Subject subject) {
        if (findById(subject.getId()).isPresent())
            throw new SubjectIdAlreadyExistsException();
        em.persist(subject);
        LOGGER.info("Created subject with id: {}", subject.getId());
        return subject;
    }

    @Override
    public void addPrerequisites(final Subject sub, final List<String> correlativesList) {
        for (String requirement : correlativesList) {
            final Subject requiredSubject = findById(requirement).orElseThrow(SubjectNotFoundException::new);
            sub.getPrerequisites().add(requiredSubject);
        }
    }

    @Override
    public SubjectClass addClassToSubject(final Subject subject, final String classCode) {
        final SubjectClass subjectClass = new SubjectClass(classCode, subject);

        if(subject.getClasses().contains(subjectClass)){
            throw new SubjectClassIdAlreadyExistsException();
        }

        subject.getClasses().add(subjectClass);
        em.persist(subjectClass);
        LOGGER.info("Added subject class with id: {} to subject with id: {}", subjectClass.getClassId(), subject.getId());
        return subjectClass;
    }

    @Override
    public List<SubjectClassTime> addClassTimesToClass(
            final SubjectClass subjectClass,
            final List<Integer> days,
            final List<LocalTime> startTimes,
            final List<LocalTime> endTimes,
            final List<String> locations,
            final List<String> buildings,
            final List<String> modes
    ) {
        final List<SubjectClassTime> classTimes = new ArrayList<>();
        for (int i = 0; i < days.size(); i++) {
            final SubjectClassTime subjectClassTime;
            if (startTimes.get(i).isAfter(endTimes.get(i))) {
                subjectClassTime = new SubjectClassTime(
                        subjectClass,
                        days.get(i),
                        endTimes.get(i),
                        startTimes.get(i),
                        locations.get(i),
                        buildings.get(i),
                        modes.get(i)
                );
            } else {
                subjectClassTime = new SubjectClassTime(
                        subjectClass,
                        days.get(i),
                        startTimes.get(i),
                        endTimes.get(i),
                        locations.get(i),
                        buildings.get(i),
                        modes.get(i)
                );
            }
            classTimes.add(subjectClassTime);
            em.persist(subjectClassTime);
            LOGGER.info("Added class times with id: {} to subject class with id: {}", subjectClassTime.getId(), subjectClass.getClassId());
        }
        return classTimes;
    }

    @Override
    public void delete(final Subject subject){
        final String id = subject.getId();
        em.createQuery("delete from Subject where id = :id")
                .setParameter("id", subject.getId())
                .executeUpdate();
        LOGGER.info("Deleted subject with id: {}", id);
    }

    @Override
    public Subject editSubject(
            final Subject subject,
            final String name,
            final String department,
            final int credits,
            final Set<Subject> prerequisites
    ){
        subject.setName(name);
        subject.setDepartment(department);
        subject.setCredits(credits);
        subject.setPrerequisites(prerequisites);

        return subject;
    }

    private void clearClassTimes(final SubjectClass subjectClass){
        subjectClass.getClassTimes().removeIf(subjectClassTime -> {
                    final long id = subjectClassTime.getId();
                    em.remove(subjectClassTime);
                    LOGGER.info("Removed class time with id: {} from subject class with id: {}", id, subjectClass.getClassId());
                    return true;
                }
        );
    }

    @Override
    public void replaceClassTimes(
            final SubjectClass subjectClass,
            final List<Integer> days,
            final List<LocalTime> startTimes,
            final List<LocalTime> endTimes,
            final List<String> locations,
            final List<String> buildings,
            final List<String> modes
    ){
        clearClassTimes(subjectClass);

        final List<SubjectClassTime> classTimes = new ArrayList<>();
        for(int i=0; i< days.size(); i++){
            // TODO: ojo con los .size()
            final SubjectClassTime subjectClassTime = new SubjectClassTime(
                    subjectClass,
                    days.get(i),
                    startTimes.get(i),
                    endTimes.get(i),
                    locations.get(i),
                    buildings.get(i),
                    modes.get(i)
            );

            classTimes.add(subjectClassTime);
            em.persist(subjectClassTime);
            LOGGER.info("Added class time with id: {} to subject class with id: {}", subjectClassTime.getId(), subjectClass.getClassId());
        }
        subjectClass.setClassTimes(classTimes);
    }

    @Override
    public void removeSubjectClassIfNotPresent(final Subject subject, final List<String> classCodes){
        subject.getClasses().removeIf(subjectClass -> {
                    if (!classCodes.contains(subjectClass.getClassId())){
                        final String id = subjectClass.getClassId();
                        em.remove(subjectClass);
                        LOGGER.info("Removed subject class with id: {} to subject with id: {}", id, subject.getId());
                        return true;
                    }
                    return false;
                }
        );
    }
    // - - - - - - - - - - SUPER SEARCH - - - - - - - - - -

    private String appendSubjectSearchParams(final StringBuilder queryString, final List<Object> paramValues, final SubjectSearchParams params){

        if(params.hasDegree()) {
            queryString.append(" s.id IN (SELECT sd.idsub FROM subjectsdegrees sd WHERE sd.iddeg = ?) AND ");
            paramValues.add(params.getDegree());
        }

        if(params.hasSemester()){
            queryString.append(" s.id IN (SELECT sd.idsub FROM subjectsdegrees sd WHERE sd.semester = ?) AND ");
            paramValues.add(params.getSemester());
        }

        if(params.hasAvailable()){
            queryString.append(" s.id IN (SELECT sd.idsub FROM subjectsdegrees sd JOIN users u ON u.degreeid = sd.iddeg WHERE u.id = ?) AND ");
            paramValues.add(params.getAvailable());

            queryString.append(" NOT EXISTS (SELECT id FROM prereqsubjects p1 WHERE p1.idsub = s.id AND " +
                    "NOT EXISTS (SELECT * FROM usersubjectprogress usp1  WHERE usp1.idsub = p1.idprereq  AND usp1.iduser = ? )) AND ");
            paramValues.add(params.getAvailable());

            queryString.append(" s.id NOT IN (SELECT usp2.idsub FROM usersubjectprogress usp2 WHERE usp2.iduser = ?) AND ");
            paramValues.add(params.getAvailable());
        }

        if(params.hasUnLockable()){
            queryString.append(" s.id IN (SELECT sd.idsub FROM subjectsdegrees sd JOIN users u ON u.degreeid = sd.iddeg WHERE u.id = ?) AND ");
            paramValues.add(params.getUnLockable());

            queryString.append(" s.id NOT IN (SELECT up.idsub FROM usersubjectprogress up WHERE up.subjectstate = 1 AND up.iduser = ?) AND ");
            paramValues.add(params.getUnLockable());

            queryString.append("s.id NOT IN  (SELECT s2.id FROM subjects s2 WHERE " +
                    " NOT EXISTS (SELECT id FROM prereqsubjects p2 WHERE p2.idsub = s2.id " +
                    " AND NOT EXISTS (SELECT * FROM usersubjectprogress usp WHERE usp.idsub = p2.idprereq AND usp.iduser = ?)) " +
                    " AND s2.id NOT IN (SELECT idsub FROM usersubjectprogress up2 WHERE up2.iduser = ?)) AND ");
            paramValues.add(params.getUnLockable());
            paramValues.add(params.getUnLockable());

            queryString.append(" NOT EXISTS ( SELECT * FROM prereqsubjects p3 WHERE s.id = p3.idsub " +
                    " AND p3.idprereq NOT IN ( SELECT up3.idsub FROM usersubjectprogress up3 WHERE up3.subjectstate = 1 AND up3.iduser = ? ) " +
                    " AND p3.idprereq NOT IN ( SELECT s3.id FROM subjects s3 WHERE NOT EXISTS ( SELECT id FROM prereqsubjects p4 WHERE p4.idsub = s3.id " +
                    " AND NOT EXISTS ( SELECT * FROM usersubjectprogress up3 WHERE up3.idsub = p4.idprereq AND up3.iduser = ? )) " +
                    " AND s3.id NOT IN (SELECT idsub FROM usersubjectprogress up4 WHERE up4.iduser = ? ))) AND ");
            paramValues.add(params.getUnLockable());
            paramValues.add(params.getUnLockable());
            paramValues.add(params.getUnLockable());
        }

        if(params.hasDone()){
            queryString.append(" s.id IN (SELECT up.idsub FROM usersubjectprogress up WHERE up.subjectstate = 1 AND up.iduser = ?) AND ");
            paramValues.add(params.getDone());
        }

        if(params.hasFuture()){
            queryString.append(" s.id IN (SELECT sd.idsub FROM subjectsdegrees sd JOIN users u ON u.degreeid = sd.iddeg WHERE u.id = ?) AND ");
            paramValues.add(params.getFuture());

            queryString.append("s.id NOT IN (SELECT up.idsub FROM usersubjectprogress up WHERE up.subjectstate = 1 AND up.iduser = ?) AND ");
            paramValues.add(params.getFuture());
        }

        if(params.hasPlan()){
            queryString.append(" s.id IN ( SELECT us.idsub FROM usersemester us WHERE us.iduser = ? AND ");
            paramValues.add(params.getPlan());

            if(params.hasPlanFinishedDate()) {
                queryString.append(" us.dateFinished = ? ) AND ");
                paramValues.add(new Timestamp(params.getPlanFinishedDate()));
            }
            else{
                queryString.append(" us.dateFinished IS NULL ) AND ");
            }
        }

        if(params.hasQuery()){
            queryString.append(" s.subname ILIKE ? AND ");
            paramValues.add("%" + sanitizeWildcards(params.getQuery()) + "%");      // TODO: move %
        }

        if(params.hasCredits()){
            queryString.append(" s.credits = ? AND ");
            paramValues.add(params.getCredits());
        }

        if(params.hasDepartment()){
            queryString.append(" s.department = ? AND ");
            paramValues.add(params.getDepartment());
        }

        if(params.hasDifficulty()){
            queryString.append(" s.id IN ( SELECT srs.idsub FROM subjectreviewstatistics srs WHERE srs.difficulty = ? ) AND ");
            paramValues.add(params.getDifficulty());
        }

        if(params.hasTimeDemand()){
            queryString.append(" s.id IN ( SELECT srs.idsub FROM subjectreviewstatistics srs WHERE srs.timedemanding = ? ) AND ");
            paramValues.add(params.getTimeDemand());
        }

        if(params.hasUserReviews()){
            queryString.append(" s.id IN ( SELECT r.idsub FROM reviews r WHERE r.iduser = ? ) AND ");
            paramValues.add(params.getUserReviews());
        }

        if(params.hasIds()){
            queryString.append(" s.id IN ( ");

            int i = 0;
            for(final String id : params.getIds()){
                queryString.append(" ? ");

                if(i < params.getIds().size() - 1){
                    queryString.append(", ");
                    i++;
                }

                paramValues.add(id);
            }

            queryString.append(" ) AND ");
        }

        return removeExcessSQL(queryString.toString());
    }
    private String removeExcessSQL(String query) {
        String and = " AND ";
        String where = " WHERE ";

        if(query.endsWith(and)){
            return query.substring(0, query.length() - and.length());
        }
        if(query.endsWith(where)){
            return query.substring(0, query.length() - where.length());
        }

        return query;
    }
    private Query createNativeQuery(final String finalizedQuery, final List<Object> paramValues){
        final Query nativeQuery = em.createNativeQuery(finalizedQuery);

        int queryParamNum = 1;
        for(Object paramValue : paramValues){
            nativeQuery.setParameter(queryParamNum++, paramValue);
        }
        return nativeQuery;
    }

    private String sanitizeWildcards(final String s) {
        return s.replaceAll("%", "\\\\%").replaceAll("_", "\\\\_");
    }

    @Override
    public List<Subject> superSearch(final SubjectSearchParams params, final int page, final SubjectOrderField orderBy, final OrderDir dir){
        // Chau performance

        final List<Object> paramValues = new ArrayList<>();
        final StringBuilder queryString = new StringBuilder("SELECT s.id FROM subjects s WHERE ");

        final String finalizedQuery = appendSubjectSearchParams(queryString, paramValues, params);

        final Query nativeQuery = createNativeQuery(finalizedQuery, paramValues);

        @SuppressWarnings("unchecked") final List<Integer> ids = nativeQuery.setFirstResult((page - 1) * PAGE_SIZE).setMaxResults(PAGE_SIZE).getResultList();

        if (ids.isEmpty()) return Collections.emptyList();

        final StringBuilder query = new StringBuilder("select s from Subject s left join s.reviewStats where id in :ids");

        appendOrderHql(query, orderBy, dir);

        return em.createQuery(query.toString(), Subject.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public int superSearchTotalPages(final SubjectSearchParams params){
        final List<Object> paramValues = new ArrayList<>();
        final StringBuilder queryString = new StringBuilder("SELECT count(*) FROM subjects s WHERE ");

        final String finalizedQuery = appendSubjectSearchParams(queryString, paramValues, params);

        final Query nativeQuery = createNativeQuery(finalizedQuery, paramValues);

        return (int) Math.max(1, Math.ceil(((Number) nativeQuery.getSingleResult()).doubleValue() / PAGE_SIZE));
    }

    public Map<SubjectFilterField, List<String>> superSearchRelevantFilters(final SubjectSearchParams params){
        final Map<SubjectFilterField, List<String>> relevant = new HashMap<>();

        for (SubjectFilterField field : SubjectFilterField.values()) {
            final List<Object> paramValues = new ArrayList<>();
            final StringBuilder queryString = new StringBuilder("SELECT DISTINCT " + field.getColumn() + " FROM subjects s LEFT JOIN subjectreviewstatistics srs ON s.id = srs.idsub WHERE ");

            final String finalizedQuery = appendSubjectSearchParams(queryString, paramValues, params);

            final Query nativeQuery = createNativeQuery(finalizedQuery, paramValues);


            @SuppressWarnings("unchecked") final List<Object> fieldValues = nativeQuery.getResultList();

            final List<String> relevantValues =
                    new HashSet<>(fieldValues)
                            .stream()
                            .filter(Objects::nonNull)
                            .sorted()
                            .map(Object::toString)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.toList());

            relevant.put(field, relevantValues);
        }

        return relevant;
    }

    // - - - - - - - - - - - - - - - -  - - - - - - - - - -

    @Override
    public Optional<Subject> findById(final String id) {
        return Optional.ofNullable(em.find(Subject.class, id));
    }


    @Override
    public List<Subject> getAll(final int page, final SubjectOrderField orderBy, final OrderDir dir) {
        final String query = "from Subject" + " order by " + orderBy.getTableColumn() + " " + dir.getQueryString();

        return em.createQuery(query, Subject.class)
                .setFirstResult((page - 1) * PAGE_SIZE)
                .setMaxResults(PAGE_SIZE)
                .getResultList();
    }

    @Override
    public Map<User, Set<Subject>> getAllUserUnreviewedNotificationSubjects() {
        @SuppressWarnings("unchecked") final List<Object[]> rows =
                em.createNativeQuery("SELECT s.id idsubject, u.id iduser FROM subjects s, users u\n" +
                                "WHERE NOT EXISTS(\n" +
                                "    SELECT * FROM reviews r\n" +
                                "    WHERE r.idsub = s.id\n" +
                                "        AND r.iduser = u.id\n" +
                                ") AND EXISTS(\n" +
                                "    SELECT * FROM usersubjectprogress usp\n" +
                                "    WHERE usp.idsub = s.id\n" +
                                "        AND usp.iduser = u.id\n" +
                                "        AND usp.subjectstate = 1\n" +
                                "        AND usp.notiftime IS NULL\n" +
                                ")")
                        .getResultList();

        if (rows.isEmpty()) return Collections.emptyMap();

        final List<String> subjectIds = rows.stream().map(row -> (String) row[0]).collect(Collectors.toList());
        final List<Long> userIds = rows.stream().map(row -> ((Number) row[1]).longValue()).collect(Collectors.toList());

        final Map<Long, User> users = em.createQuery("from User where id in :ids", User.class)
                .setParameter("ids", userIds)
                .getResultList()
                .stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        final Map<String, Subject> subjects = em.createQuery("from Subject where id in :ids", Subject.class)
                .setParameter("ids", subjectIds)
                .getResultList()
                .stream()
                .collect(Collectors.toMap(Subject::getId, Function.identity()));

        final Map<User, Set<Subject>> result = new HashMap<>();
        for (final Object[] row : rows) {
            final User user = users.get(((Number) row[1]).longValue());
            final Subject subject = subjects.get((String) row[0]);

            final Set<Subject> userSubjects = result.getOrDefault(user, new HashSet<>());
            userSubjects.add(subject);
            result.putIfAbsent(user, userSubjects);
        }

        return result;
    }

    @Override
    public void updateUnreviewedNotificationTime() {
        em.createNativeQuery("UPDATE usersubjectprogress SET notiftime = now() WHERE subjectstate = 1 AND notiftime IS NULL")
                .executeUpdate();
    }
    private void appendOrderHql(final StringBuilder sb, final SubjectOrderField orderBy, final OrderDir dir) {
        if (orderBy == null) return;

        OrderDir dirToUse = dir;
        if (dir == null) dirToUse = OrderDir.ASCENDING;

        sb.append(" order by ")
                .append(orderBy.getFieldName())
                .append(" ")
                .append(dirToUse.getQueryString());
    }
}
