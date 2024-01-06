package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.OrderDir;
import ar.edu.itba.paw.models.enums.SubjectFilterField;
import ar.edu.itba.paw.models.enums.SubjectOrderField;
import ar.edu.itba.paw.models.exceptions.SubjectIdAlreadyExistsException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class SubjectJpaDao implements SubjectDao {
    private static final int PAGE_SIZE = 20;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Subject create(final Subject subject) {
        if(findById(subject.getId()).isPresent())
            throw new SubjectIdAlreadyExistsException();
        em.persist(subject);
        return subject;
    }

    @Override
    public Optional<Subject> findById(final String id) {
        return Optional.ofNullable(em.find(Subject.class, id));
    }

    @Override
    public List<Subject> findAllThatUserCanDo(final User user, final int page, final SubjectOrderField orderBy, final OrderDir dir) {
        @SuppressWarnings("unchecked")
        final List<Integer> ids =
        em.createNativeQuery("SELECT id FROM subjects s FULL JOIN subjectsdegrees sd ON s.id = sd.idsub\n" +
                        "WHERE sd.iddeg = ?\n " +
                        "AND NOT EXISTS ( \n" +
                        "    SELECT id FROM prereqsubjects p \n" +
                        "    WHERE p.idsub = s.id \n" +
                        "    AND NOT EXISTS ( \n" +
                        "        SELECT * FROM usersubjectprogress usp \n" +
                        "        WHERE usp.idsub = p.idprereq \n" +
                        "        AND usp.iduser = ? \n" +
                        "    ) \n" +
                        ") \n" +
                        "AND s.id NOT IN ( \n" +
                        "    SELECT idsub FROM usersubjectprogress usp \n" +
                        "    WHERE usp.iduser = ? \n" +
                        ")")
        .setParameter(1,user.getDegree().getId())
        .setParameter(2, user.getId())
        .setParameter(3,user.getId())
        .setFirstResult((page - 1) * PAGE_SIZE)
        .setMaxResults(PAGE_SIZE)
        .getResultList();

        if(ids.isEmpty()) return Collections.emptyList();

        final StringBuilder query = new StringBuilder("from Subject s where s.id in :ids");

        appendOrderHql(query, orderBy, dir);

        return em.createQuery(query.toString(), Subject.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public List<Subject> findAllThatUserHasNotDone(final User user, final int page, final SubjectOrderField orderBy, final OrderDir dir){
        @SuppressWarnings("unchecked")
        final List<Integer> ids =
        em.createNativeQuery("SELECT id FROM subjects s FULL JOIN subjectsdegrees sd ON s.id = sd.idsub\n" +
                        "WHERE sd.iddeg = ?\n " +
                        "AND NOT EXISTS ( \n" +
                        "    SELECT * \n" +
                        "FROM usersubjectprogress up \n" +
                        "WHERE up.idsub = s.id AND up.subjectstate = 1 AND up.iduser = ? \n" +
                        ")")
        .setParameter(1, user.getDegree().getId())
        .setParameter(2, user.getId())
        .setFirstResult((page - 1) * PAGE_SIZE)
        .setMaxResults(PAGE_SIZE)
        .getResultList();

        if(ids.isEmpty()) return Collections.emptyList();

        final StringBuilder query = new StringBuilder("from Subject s where s.id in :ids");

        appendOrderHql(query, orderBy, dir);

        return em.createQuery(query.toString(), Subject.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public List<Subject> findAllThatUserHasDone(final User user, final int page, final SubjectOrderField orderBy, final OrderDir dir) {
        @SuppressWarnings("unchecked")
        final List<Integer> ids =
                em.createNativeQuery("SELECT up.idsub\n" +
                                "\tFROM usersubjectprogress up\n" +
                                "\tWHERE up.subjectstate = 1 AND up.iduser = ?\n")
                        .setParameter(1, user.getId())
                        .setFirstResult((page - 1) * PAGE_SIZE)
                        .setMaxResults(PAGE_SIZE)
                        .getResultList();

        if(ids.isEmpty()) return Collections.emptyList();

        final StringBuilder query = new StringBuilder("from Subject s where s.id in :ids");

        appendOrderHql(query, orderBy, dir);

        return em.createQuery(query.toString(), Subject.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public List<Subject> findAllThatUserCouldUnlock(final User user, final int page, final SubjectOrderField orderBy, final OrderDir dir){
        @SuppressWarnings("unchecked")
        final List<Integer> ids = em.createNativeQuery("SELECT DISTINCT s.id\n" +
                        "FROM subjects s FULL JOIN subjectsdegrees sd on s.id = sd.idsub\n" +
                        "WHERE sd.iddeg = ? " +
                        "AND s.id NOT IN (\n" +
                        "    SELECT up.idsub\n" +
                        "    FROM usersubjectprogress up\n" +
                        "    WHERE up.subjectstate = 1 AND up.iduser = ?\n" +
                        ")\n" +
                        "  AND s.id NOT IN  (\n" +
                        "    SELECT s2.id\n" +
                        "    FROM subjects s2\n" +
                        "    WHERE NOT EXISTS (\n" +
                        "            SELECT id FROM prereqsubjects p2\n" +
                        "            WHERE p2.idsub = s2.id\n" +
                        "              AND NOT EXISTS (\n" +
                        "                    SELECT * FROM usersubjectprogress usp\n" +
                        "                    WHERE usp.idsub = p2.idprereq\n" +
                        "                      AND usp.iduser = ?\n" +
                        "                )\n" +
                        "        )\n" +
                        "      AND s2.id NOT IN (\n" +
                        "        SELECT idsub FROM usersubjectprogress up2\n" +
                        "        WHERE up2.iduser = ?\n" +
                        "    )\n" +
                        ")\n" +
                        "AND NOT EXISTS (\n" +
                        "    SELECT *\n" +
                        "    FROM prereqsubjects p3\n" +
                        "    WHERE s.id = p3.idsub\n" +
                        "    AND p3.idprereq NOT IN (\n" +
                        "        SELECT up3.idsub\n" +
                        "        FROM usersubjectprogress up3\n" +
                        "        WHERE up3.subjectstate = 1 AND up3.iduser = ?\n" +
                        "        )\n" +
                        "    AND p3.idprereq NOT IN (\n" +
                        "        SELECT s3.id\n" +
                        "        FROM subjects s3\n" +
                        "        WHERE NOT EXISTS (\n" +
                        "                SELECT id FROM prereqsubjects p4\n" +
                        "                WHERE p4.idsub = s3.id\n" +
                        "                  AND NOT EXISTS (\n" +
                        "                        SELECT * FROM usersubjectprogress up3\n" +
                        "                        WHERE up3.idsub = p4.idprereq\n" +
                        "                          AND up3.iduser = ?\n" +
                        "                    )\n" +
                        "            )\n" +
                        "          AND s3.id NOT IN (\n" +
                        "            SELECT idsub FROM usersubjectprogress up4\n" +
                        "            WHERE up4.iduser = ?\n" +
                        "        )\n" +
                        "        )\n" +
                        ")")
                .setParameter(1, user.getDegree().getId())
                .setParameter(2, user.getId())
                .setParameter(3, user.getId())
                .setParameter(4, user.getId())
                .setParameter(5, user.getId())
                .setParameter(6, user.getId())
                .setParameter(7, user.getId())
                .setFirstResult((page - 1) * PAGE_SIZE)
                .setMaxResults(PAGE_SIZE)
                .getResultList();

        if(ids.isEmpty()) return Collections.emptyList();

        final StringBuilder query = new StringBuilder("from Subject s where s.id in :ids");

        appendOrderHql(query, orderBy, dir);

        return em.createQuery(query.toString(), Subject.class)
                .setParameter("ids", ids)
                .getResultList();
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
    public List<Subject> search(final User user, final String name, final int page) {
        return search(user, name, page, null, null, null);
    }

    private List<Subject> searchSpecific(
            final boolean searchAll,
            final User user,
            final String name,
            final int page,
            final Map<SubjectFilterField, String> filters,
            final SubjectOrderField orderBy,
            final OrderDir dir
    ){
        final StringBuilder nativeQuerySb;

        if(searchAll){
            nativeQuerySb = new StringBuilder("SELECT s.id FROM subjects s LEFT JOIN subjectreviewstatistics srs ON s.id = srs.idsub WHERE subname ILIKE ?");
        }
        else{
            nativeQuerySb = new StringBuilder("SELECT s.id FROM subjects s LEFT JOIN subjectreviewstatistics srs ON s.id = srs.idsub LEFT JOIN subjectsdegrees sd ON s.id = sd.idsub WHERE sd.iddeg = ? AND subname ILIKE ?");
        }

        List<Object> filterValues = null;
        if (filters != null) {
            filterValues = appendFiltersAndGetValues(nativeQuerySb, filters, orderBy);
        }

        appendOrderSql(nativeQuerySb, orderBy, dir);

        final Query nativeQuery = em.createNativeQuery(nativeQuerySb.toString());

        int offset;
        if(!searchAll){
            nativeQuery.setParameter(1, user.getDegree().getId());
            nativeQuery.setParameter(2, "%" + sanitizeWildcards(name) + "%");
            offset = 3;
        }
        else{
            nativeQuery.setParameter(1, "%" + sanitizeWildcards(name) + "%");
            offset = 2;
        }

        if (filters != null) {
            for (int i = 0; i < filterValues.size(); i++) {
                nativeQuery.setParameter(i + offset, filterValues.get(i));
            }
        }

        @SuppressWarnings("unchecked") final List<String> ids = nativeQuery
                .setFirstResult((page - 1) * PAGE_SIZE)
                .setMaxResults(PAGE_SIZE)
                .getResultList();

        if(ids.isEmpty()) return Collections.emptyList();

        final StringBuilder hqlQuerySb;

        hqlQuerySb = new StringBuilder("select s from Subject s left join s.reviewStats where id in :ids");
        appendOrderHql(hqlQuerySb, orderBy, dir);

        return em.createQuery(hqlQuerySb.toString(), Subject.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public List<Subject> search(
            final User user,
            final String name,
            final int page,
            final Map<SubjectFilterField, String> filters,
            final SubjectOrderField orderBy, OrderDir dir
    ) {
        return searchSpecific(false, user, name, page, filters, orderBy, dir);
    }

    @Override
    public List<Subject> searchAll(
            final String name,
            final int page,
            final Map<SubjectFilterField, String> filters,
            final SubjectOrderField orderBy, OrderDir dir
    ){
        return searchSpecific(true, null, name, page, filters, orderBy, dir);
    }

    private int getTotalPagesForSearchSpecific(
            final boolean searchAll,
            final User user,
            final String name,
            final Map<SubjectFilterField, String> filters,
            final SubjectOrderField orderBy
    ) {
        final StringBuilder nativeQuerySb;
        if(searchAll){
            nativeQuerySb = new StringBuilder("SELECT count(*) FROM subjects s LEFT JOIN subjectreviewstatistics srs ON s.id = srs.idsub WHERE subname ILIKE ?");
        }
        else{
            nativeQuerySb = new StringBuilder("SELECT count(*) FROM subjects s LEFT JOIN subjectreviewstatistics srs ON s.id = srs.idsub LEFT JOIN subjectsdegrees sd ON s.id = sd.idsub WHERE sd.iddeg = ? AND subname ILIKE ?");
        }

        List<Object> filterValues = null;
        if (filters != null) {
            filterValues = appendFiltersAndGetValues(nativeQuerySb, filters, orderBy);
        }

        final Query nativeQuery = em.createNativeQuery(nativeQuerySb.toString());

        int offset;
        if(!searchAll){
            nativeQuery.setParameter(1, user.getDegree().getId());
            nativeQuery.setParameter(2, "%" + sanitizeWildcards(name) + "%");
            offset = 3;
        }
        else{
            nativeQuery.setParameter(1, "%" + sanitizeWildcards(name) + "%");
            offset = 2;
        }


        if (filters != null) {
            for (int i = 0; i < filterValues.size(); i++) {
                nativeQuery.setParameter(i + offset, filterValues.get(i));
            }
        }

        return (int) Math.max(1, Math.ceil(((Number) nativeQuery.getSingleResult()).doubleValue() / PAGE_SIZE));
    }

    @Override
    public int getTotalPagesForSearch(
            final User user,
            final String name,
            final Map<SubjectFilterField, String> filters,
            final SubjectOrderField orderBy
    ) {
        return getTotalPagesForSearchSpecific(false, user, name, filters, orderBy);
    }

    @Override
    public int getTotalPagesForSearchAll(
            final String name,
            final Map<SubjectFilterField, String> filters,
            final SubjectOrderField orderBy
    ) {
        return getTotalPagesForSearchSpecific(true, null, name, filters, orderBy);
    }

    private Map<SubjectFilterField, List<String>> getRelevantFiltersForSearchSpecific(
            final Degree degree,
            final String name,
            final Map<SubjectFilterField, String> filters,
            final SubjectOrderField orderBy
    ) {
        final Map<SubjectFilterField, List<String>> relevant = new HashMap<>();

        for (SubjectFilterField field : SubjectFilterField.values()) {
            final StringBuilder nativeQuerySb;
            if(degree != null) {
                nativeQuerySb = new StringBuilder("SELECT DISTINCT ").append(field.getColumn()).append(
                        " FROM subjects s LEFT JOIN subjectreviewstatistics srs ON s.id = srs.idsub LEFT JOIN subjectsdegrees sd ON s.id = sd.idsub WHERE sd.iddeg = ? AND subname ILIKE ?"
                );
            }
            else {
                nativeQuerySb = new StringBuilder("SELECT DISTINCT ").append(field.getColumn()).append(
                            " FROM subjects s LEFT JOIN subjectreviewstatistics srs ON s.id = srs.idsub WHERE subname ILIKE ?"
                );
            }

            List<Object> filterValues = null;
            if (filters != null) {
                filterValues = appendFiltersAndGetValues(nativeQuerySb, filters, orderBy);
            }

            final Query nativeQuery = em.createNativeQuery(nativeQuerySb.toString());

            int offset = degree == null ? 2 : 3;
            if (filters != null) {
                for (int i = 0; i < filterValues.size(); i++) {
                    nativeQuery.setParameter(i + offset, filterValues.get(i));
                }
            }
            if(degree != null){
                nativeQuery.setParameter(1, degree.getId());
                nativeQuery.setParameter(2, "%" + sanitizeWildcards(name) + "%");
            }
            else{
                nativeQuery.setParameter(1, "%" + sanitizeWildcards(name) + "%");
            }

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

    @Override
    public Map<SubjectFilterField, List<String>> getRelevantFiltersForSearch(
            final Degree degree,
            final String name,
            final Map<SubjectFilterField, String> filters,
            final SubjectOrderField orderBy
    ){
        return getRelevantFiltersForSearchSpecific(degree, name, filters, orderBy);
    }

    @Override
    public Map<SubjectFilterField, List<String>> getRelevantFiltersForSearch(
            final String name,
            final Map<SubjectFilterField, String> filters,
            final SubjectOrderField orderBy
    ) {
        return getRelevantFiltersForSearchSpecific(null, name, filters, orderBy);
    }

    @Override
    public Map<User, Set<Subject>> getAllUserUnreviewedNotificationSubjects() {
        @SuppressWarnings("unchecked")
        final List<Object[]> rows =
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

        if(rows.isEmpty()) return Collections.emptyMap();

        final List<String> subjectIds = rows.stream().map(row -> (String)row[0]).collect(Collectors.toList());
        final List<Long> userIds = rows.stream().map(row -> ((Number)row[1]).longValue()).collect(Collectors.toList());

        final Map<Long,User> users = em.createQuery("from User where id in :ids", User.class)
                .setParameter("ids", userIds)
                .getResultList()
                .stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        final Map<String,Subject> subjects = em.createQuery("from Subject where id in :ids", Subject.class)
                .setParameter("ids", subjectIds)
                .getResultList()
                .stream()
                .collect(Collectors.toMap(Subject::getId, Function.identity()));

        final Map<User, Set<Subject>> result = new HashMap<>();
        for(final Object[] row : rows) {
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

    private String sanitizeWildcards(final String s) {
        return s.replaceAll("%", "\\\\%").replaceAll("_", "\\\\_");
    }

    private void appendFilters(final StringBuilder sb, final Map<SubjectFilterField, String> filters, final SubjectOrderField orderBy) {
        if(orderBy != null) {
            if(orderBy.equals(SubjectOrderField.DIFFICULTY)) {
                sb.append(" AND difficulty IS NOT NULL");
            }
            else if(orderBy.equals(SubjectOrderField.TIME)) {
                sb.append(" AND timeDemanding IS NOT NULL");
            }
        }

        for (SubjectFilterField field : filters.keySet()) {
            sb.append(" AND ")
                    .append(field.getColumn())
                    .append(" = ?");
        }
    }

    private List<Object> appendFiltersAndGetValues(
            final StringBuilder sb,
            final Map<SubjectFilterField, String> filters,
            final SubjectOrderField orderBy
    ) {
        appendFilters(sb, filters, orderBy);

        final List<Object> filterValues = new ArrayList<>();
        for (Map.Entry<SubjectFilterField, String> entry : filters.entrySet()) {
            if (entry.getKey() == SubjectFilterField.CREDITS ||
                    entry.getKey() == SubjectFilterField.DIFFICULTY ||
                    entry.getKey() == SubjectFilterField.TIME
            ) {
                filterValues.add(Integer.parseInt(entry.getValue()));
            } else {
                filterValues.add(entry.getValue());
            }
        }

        return filterValues;
    }

    private void appendOrderSql(final StringBuilder sb, final SubjectOrderField orderBy, final OrderDir dir) {
        if (orderBy == null) return;

        OrderDir dirToUse = dir;
        if (dir == null) dirToUse = OrderDir.ASCENDING;

        sb.append(" ORDER BY ")
                .append(orderBy.getTableColumn())
                .append(" ")
                .append(dirToUse.getQueryString());
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

    @Override
    public void addPrerequisites(final Subject sub, final List<String> correlativesList){
        for( String requirement : correlativesList ){
            final Subject requiredSubject = em.find(Subject.class, requirement);
            sub.getPrerequisites().add(requiredSubject);
        }
    }
    @Override
    public void updatePrerequisitesToAdd(final Subject sub, final List<Subject> correlativesList) {
        final Set<Subject> prerequisites = sub.getPrerequisites();
        prerequisites.addAll(correlativesList);
    }

    @Override
    public void updatePrerequisitesToRemove(final Subject sub, final List<Subject> correlativesList) {
        final Set<Subject> prerequisites = sub.getPrerequisites();
        correlativesList.forEach(prerequisites::remove);
    }

    @Override
    public void addClassesToSubject(final Subject subject, final Set<String> classesSet){
        for(final String classCode : classesSet){
            final SubjectClass subjectClass = new SubjectClass(classCode, subject);
            subject.getClasses().add(subjectClass);
            em.persist(subjectClass);
        }
    }

    @Override
    public SubjectClass addClassToSubject(final Subject subject, final String classCode){
        final SubjectClass subjectClass = new SubjectClass(classCode, subject);
        subject.getClasses().add(subjectClass);
        em.persist(subjectClass);
        return subjectClass;
    }

    @Override
    public void addClassTimesToClass(
            final SubjectClass subjectClass,
            final List<Integer> days,
            final List<LocalTime> startTimes,
            final List<LocalTime> endTimes,
            final List<String> locations,
            final List<String> buildings,
            final List<String> modes
    ){
        for(int i=0; i<days.size(); i++){
            final SubjectClassTime subjectClassTime;
            if( startTimes.get(i).isAfter(endTimes.get(i)) ){
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
            em.persist(subjectClassTime);
        }
    }

    @Override
    public void addSubjectClassTimes(
            final Subject subject,
            final List<String> classCodes,
            final List<LocalTime> startTimes,
            final List<LocalTime> endTimes,
            final List<String> buildings,
            final List<String> modes,
            final List<Integer> days,
            final List<String> rooms
    ) {
        for( int i = 0 ; i < classCodes.size() ; i++) {
            final SubjectClass subjectClass = subject.getClassesById().get(classCodes.get(i));
            final SubjectClassTime subjectClassTime;
            if( startTimes.get(i).isAfter(endTimes.get(i)) ){
                subjectClassTime = new SubjectClassTime(
                        subjectClass,
                        days.get(i),
                        endTimes.get(i),
                        startTimes.get(i),
                        rooms.get(i),
                        buildings.get(i),
                        modes.get(i)
                );
            } else {
                subjectClassTime = new SubjectClassTime(
                        subjectClass,
                        days.get(i),
                        startTimes.get(i),
                        endTimes.get(i),
                        rooms.get(i),
                        buildings.get(i),
                        modes.get(i)
                );
            }
            em.persist(subjectClassTime);
        }
    }


    @Override
    public void createClassLocTime(
            final SubjectClass subjectClass,
            final int days,
            final LocalTime endTimes,
            final LocalTime startTimes,
            final String rooms,
            final String buildings,
            final String modes
    ){
        final SubjectClassTime subjectClassTime;

        if(startTimes.isAfter(endTimes)) {
            subjectClassTime = new SubjectClassTime(subjectClass, days, endTimes, startTimes, rooms, buildings, modes);
        } else {
            subjectClassTime = new SubjectClassTime(subjectClass, days, startTimes, endTimes, rooms, buildings, modes);
        }
        subjectClass.getClassTimes().add(subjectClassTime);
        em.persist(subjectClassTime);
    }

    @Override
    public void deleteClassLocTime(final long key) {
        final SubjectClassTime subjectClassTime = em.find(SubjectClassTime.class, key);
        final SubjectClass subjectClass = subjectClassTime.getSubjectClass();

        subjectClass.getClassTimes().remove(subjectClassTime);
    }

    @Override
    public void deleteClass(final SubjectClass subjectClass) {
        final Subject subject = subjectClass.getSubject();
        subject.getClasses().remove(subjectClass);
    }

    @Override
    public void updateClassLocTime(
            final long key,
            final int days,
            final String rooms,
            final String buildings,
            final String modes,
            final LocalTime startTimes,
            final LocalTime endTimes
    ){
        final SubjectClassTime subjectClassTime = em.find(SubjectClassTime.class, key);
        subjectClassTime.setDay(days);
        subjectClassTime.setClassLoc(rooms);
        subjectClassTime.setBuilding(buildings);
        subjectClassTime.setMode(modes);
        if(startTimes.isAfter(endTimes)) {
            subjectClassTime.setStartTime(endTimes);
            subjectClassTime.setEndTime(startTimes);
        } else {
            subjectClassTime.setStartTime(startTimes);
            subjectClassTime.setEndTime(endTimes);
        }
    }

    @Override
    public void delete(final Subject subject){
        em.createQuery("delete from Subject where id = :id")
                .setParameter("id", subject.getId())
                .executeUpdate();
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
                em.remove(subjectClassTime);
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
        }
        subjectClass.setClassTimes(classTimes);
    }

    @Override
    public void removeSubjectClassIfNotPresent(final Subject subject, final List<String> classCodes){
        subject.getClasses().removeIf(subjectClass -> {
                if (!classCodes.contains(subjectClass.getClassId())){
                    em.remove(subjectClass);
                    return true;
                }
                return false;
            }
        );
    }
}
