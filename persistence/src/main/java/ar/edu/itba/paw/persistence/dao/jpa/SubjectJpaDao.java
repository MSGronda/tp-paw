package ar.edu.itba.paw.persistence.dao.jpa;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.OrderDir;
import ar.edu.itba.paw.models.enums.SubjectFilterField;
import ar.edu.itba.paw.models.enums.SubjectOrderField;
import ar.edu.itba.paw.persistence.dao.SubjectDao;
import ar.edu.itba.paw.persistence.exceptions.SubjectIdAlreadyExistsPersistenceException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class SubjectJpaDao implements SubjectDao {
    private static final int PAGE_SIZE = 20;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Subject create(final Subject subject) throws SubjectIdAlreadyExistsPersistenceException {
        if(findById(subject.getId()).isPresent())
            throw new SubjectIdAlreadyExistsPersistenceException();
        em.persist(subject);
        return subject;
    }

    @Override
    public Optional<Subject> findById(final String id) {
        return Optional.ofNullable(em.find(Subject.class, id));
    }

    @Override
    public List<Subject> findAllThatUserCanDo(User user) {
        @SuppressWarnings("unchecked") final List<Integer> ids =
                em.createNativeQuery("SELECT id FROM subjects s\n" +
                                "WHERE NOT EXISTS (\n" +
                                "    SELECT id FROM prereqsubjects p\n" +
                                "    WHERE p.idsub = s.id\n" +
                                "    AND NOT EXISTS (\n" +
                                "        SELECT * FROM usersubjectprogress usp\n" +
                                "        WHERE usp.idsub = p.idprereq\n" +
                                "        AND usp.iduser = ?\n" +
                                "    )\n" +
                                ")\n" +
                                "AND s.id NOT IN (\n" +
                                "    SELECT idsub FROM usersubjectprogress usp\n" +
                                "    WHERE usp.iduser = ?\n" +
                                ")")
                .setParameter(1, user.getId())
                .setParameter(2,user.getId())
                .getResultList();

        if(ids.isEmpty()) return Collections.emptyList();

        return em.createQuery("from Subject s where s.id in :ids", Subject.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public List<Subject> findAllThatUserHasNotDone(User user){
        @SuppressWarnings("unchecked")
        final List<Integer> ids =
                em.createNativeQuery("SELECT id\n" +
                        "FROM subjects s\n" +
                        "WHERE NOT EXISTS (\n" +
                        "    SELECT *\n" +
                        "\tFROM usersubjectprogress up\n" +
                        "\tWHERE up.idsub = s.id AND up.subjectstate = 1 AND up.iduser = ?\n" +
                        ")\n")
                .setParameter(1, user.getId())
                .getResultList();

        if(ids.isEmpty()) return Collections.emptyList();

        return em.createQuery("from Subject s where s.id in :ids", Subject.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public List<Subject> findAllThatUserHasDone(User user) {
        @SuppressWarnings("unchecked")
        final List<Integer> ids =
                em.createNativeQuery("SELECT up.idsub\n" +
                                "\tFROM usersubjectprogress up\n" +
                                "\tWHERE up.subjectstate = 1 AND up.iduser = ?\n")
                        .setParameter(1, user.getId())
                        .getResultList();

        if(ids.isEmpty()) return Collections.emptyList();

        return em.createQuery("from Subject s where s.id in :ids", Subject.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public List<Subject> findAllThatUserCouldUnlock(User user){
        @SuppressWarnings("unchecked")
        final List<Integer> ids = em.createNativeQuery("SELECT DISTINCT s.id\n" +
                        "FROM subjects s\n" +
                        "WHERE s.id NOT IN (\n" +
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
                .setParameter(1, user.getId())
                .setParameter(2, user.getId())
                .setParameter(3, user.getId())
                .setParameter(4, user.getId())
                .setParameter(5, user.getId())
                .setParameter(6, user.getId())
                .getResultList();

        if(ids.isEmpty()) return Collections.emptyList();

        return em.createQuery("from Subject s where s.id in :ids", Subject.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public List<Subject> getAll() {
        return em.createQuery("from Subject", Subject.class)
                .getResultList();
    }

    @Override
    public List<Subject> search(final String name, final int page) {
        return search(name, page, null, null, null);
    }

    @Override
    public List<Subject> search(
            final String name,
            final int page,
            final Map<SubjectFilterField, String> filters,
            final SubjectOrderField orderBy, OrderDir dir
    ) {
        //TODO: implement using CriteriaBuilder

        final StringBuilder nativeQuerySb = new StringBuilder("SELECT id FROM subjects WHERE subname ILIKE ?");

        List<Object> filterValues = null;
        if (filters != null) {
            filterValues = appendFiltersAndGetValues(nativeQuerySb, filters);
        }

        appendOrderSql(nativeQuerySb, orderBy, dir);

        final Query nativeQuery = em.createNativeQuery(nativeQuerySb.toString());
        nativeQuery.setParameter(1, "%" + sanitizeWildcards(name) + "%");

        if (filters != null) {
            for (int i = 0; i < filterValues.size(); i++) {
                nativeQuery.setParameter(i + 2, filterValues.get(i));
            }
        }

        @SuppressWarnings("unchecked") final List<String> ids = nativeQuery
                .setFirstResult((page - 1) * PAGE_SIZE)
                .setMaxResults(PAGE_SIZE)
                .getResultList();

        if(ids.isEmpty()) return Collections.emptyList();

        final StringBuilder hqlQuerySb = new StringBuilder("from Subject where id in :ids");
        appendOrderHql(hqlQuerySb, orderBy, dir);

        return em.createQuery(hqlQuerySb.toString(), Subject.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public int getTotalPagesForSearch(final String name, final Map<SubjectFilterField, String> filters) {
        final StringBuilder nativeQuerySb = new StringBuilder("SELECT count(*) FROM subjects WHERE subname ILIKE ?");

        List<Object> filterValues = null;
        if (filters != null) {
            filterValues = appendFiltersAndGetValues(nativeQuerySb, filters);
        }

        final Query nativeQuery = em.createNativeQuery(nativeQuerySb.toString());
        nativeQuery.setParameter(1, "%" + sanitizeWildcards(name) + "%");

        if (filters != null) {
            for (int i = 0; i < filterValues.size(); i++) {
                nativeQuery.setParameter(i + 2, filterValues.get(i));
            }
        }

        return (int) Math.max(1, Math.ceil(((Number) nativeQuery.getSingleResult()).doubleValue() / PAGE_SIZE));
    }

    @Override
    public Map<SubjectFilterField, List<String>> getRelevantFiltersForSearch(final String name, final Map<SubjectFilterField, String> filters) {
        final Map<SubjectFilterField, List<String>> relevant = new HashMap<>();

        for (SubjectFilterField field : SubjectFilterField.values()) {
            final StringBuilder nativeQuerySb =
                    new StringBuilder("SELECT DISTINCT ").append(field.getColumn()).append(" FROM subjects WHERE subname ILIKE ?");

            List<Object> filterValues = null;
            if (filters != null) {
                filterValues = appendFiltersAndGetValues(nativeQuerySb, filters);
            }

            final Query nativeQuery = em.createNativeQuery(nativeQuerySb.toString());

            if (filters != null) {
                for (int i = 0; i < filterValues.size(); i++) {
                    nativeQuery.setParameter(i + 2, filterValues.get(i));
                }
            }

            @SuppressWarnings("unchecked") final List<Object> fieldValues = nativeQuery
                    .setParameter(1, "%" + sanitizeWildcards(name) + "%")
                    .getResultList();

            final List<String> relevantValues =
                    new HashSet<>(fieldValues)
                            .stream()
                            .sorted()
                            .map(Object::toString)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.toList());

            relevant.put(field, relevantValues);
        }

        return relevant;
    }

    @Override
    public Map<User, Set<Subject>> getAllUserUnreviewedNotificationSubjects() {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public void updateUnreviewedNotificationTime() {
        //TODO
        throw new NotImplementedException();
    }

    private String sanitizeWildcards(final String s) {
        return s.replaceAll("%", "\\\\%").replaceAll("_", "\\\\_");
    }

    private void appendFilters(final StringBuilder sb, final Map<SubjectFilterField, String> filters) {
        for (SubjectFilterField field : filters.keySet()) {
            sb.append(" AND ")
                    .append(field.getColumn())
                    .append(" = ?");
        }
    }

    private List<Object> appendFiltersAndGetValues(final StringBuilder sb, final Map<SubjectFilterField, String> filters) {
        appendFilters(sb, filters);

        final List<Object> filterValues = new ArrayList<>();
        for (Map.Entry<SubjectFilterField, String> entry : filters.entrySet()) {
            if (entry.getKey() == SubjectFilterField.CREDITS) {
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
    public void addPrerequisites(Subject sub, List<String> correlativesList){
        for( String requirement : correlativesList ){
            Subject requiredSubject = em.find(Subject.class, requirement);
            sub.getPrerequisites().add(requiredSubject);
        }
    }

}
