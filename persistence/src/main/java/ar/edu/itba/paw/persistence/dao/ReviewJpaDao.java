package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewVote;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.OrderDir;
import ar.edu.itba.paw.models.enums.ReviewOrderField;
import ar.edu.itba.paw.models.enums.ReviewVoteType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ReviewJpaDao implements ReviewDao {
    private final static Logger LOGGER = LoggerFactory.getLogger(ReviewJpaDao.class);
    private static final int PAGE_SIZE = 10;
    @PersistenceContext
    private EntityManager em;

    @Override
    public Review create(final Review.Builder reviewBuilder) {
        final Review review = reviewBuilder.build();
        em.persist(review);

        LOGGER.info("Created review with id: {}", review.getId());
        return review;
    }

    @Override
    public Review update(final Review.Builder reviewBuilder) {
        final Review updated = reviewBuilder.build();
        final Review r = em.find(Review.class, updated.getId());
        r.setAnonymous(updated.isAnonymous());
        r.setText(updated.getText());
        r.setDifficulty(updated.getDifficulty());
        r.setTimeDemanding(updated.getTimeDemanding());
        LOGGER.info("Updated review with id: {}", reviewBuilder.getId());
        return r;
    }

    @Override
    public void delete(final Review review) {
        final long id = review.getId();
        em.remove(review);
        LOGGER.info("Deleted review with id: {}", id);
    }

    @Override
    public Optional<Review> findById(final long id) {
        return Optional.ofNullable(em.find(Review.class, id));
    }

    @Override
    public ReviewVote voteReview(final User user, final Review review, final ReviewVoteType vote){
        final Optional<ReviewVote> maybeReviewVote = getUserVote(user, review);

        if(maybeReviewVote.isPresent()) {
            final ReviewVote reviewVote = maybeReviewVote.get();
            reviewVote.setVote(vote);
            return reviewVote;
        }

        final ReviewVote reviewVote = new ReviewVote(user, review, vote);
        em.persist(reviewVote);

        return reviewVote;
    }

    @Override
    public void deleteReviewVote(final User user, final Review review){
        final Optional<ReviewVote> maybeReviewVote = getUserVote(user, review);
        maybeReviewVote.ifPresent(reviewVote -> em.remove(reviewVote));
    }

    @Override
    public Optional<ReviewVote> getUserVote(final User user, final Review review){
        return em.createQuery("from ReviewVote where user = :user and review = :review", ReviewVote.class)
                .setParameter("user", user)
                .setParameter("review", review)
                .getResultList()
                .stream().findFirst();
    }

    @Override
    public List<ReviewVote> getReviewVotes(final Review review, final int page){
        return em.createQuery("from ReviewVote where review = :review order by vote", ReviewVote.class)
                .setParameter("review", review)
                .setFirstResult((page - 1) * PAGE_SIZE)
                .setMaxResults(PAGE_SIZE)
                .getResultList();
    }



    @Override
    public boolean didUserVote(final User user, final Review review) {
        return !em.createQuery("from ReviewVote where user = :user and review = :review", ReviewVote.class)
                .setParameter("user", user)
                .setParameter("review", review)
                .getResultList().isEmpty();
    }

    @Override
    public boolean didUserReview(final Subject subject, final User user) {
        return !em.createQuery("from Review where subject = :subject and user = :user", Review.class)
                .setParameter("subject", subject)
                .setParameter("user", user)
                .getResultList().isEmpty();
    }

    private StringBuilder removeExcessSQL(StringBuilder query) {
        String and = " AND ";
        String where = " WHERE ";

        if(query.toString().endsWith(and)){
            return new StringBuilder(query.substring(0, query.length() - and.length()));
        }
        if(query.toString().endsWith(where)){
            return new StringBuilder(query.substring(0, query.length() - where.length()));
        }
        return query;
    }

    private StringBuilder addSearchFilters(
            final StringBuilder nativeQuerySb,
            final List<Object> params,
            final User currentUser,
            final String subjectId,
            final Long userId
    ){
        if(subjectId != null) {
            nativeQuerySb.append(" idsub = ? AND ");
            params.add(subjectId);
        }
        if(userId != null) {
            nativeQuerySb.append(" iduser = ?  AND ");

            if(userId != currentUser.getId()){
                nativeQuerySb.append(" useranonymous IS NOT TRUE AND ");
            }

            params.add(userId);
        }
        return removeExcessSQL(nativeQuerySb);
    }

    private StringBuilder appendOrderSql(final StringBuilder sb, final ReviewOrderField orderBy, final OrderDir dir) {
        if (orderBy == null) return sb;

        OrderDir dirToUse = dir;
        if (dir == null) dirToUse = OrderDir.ASCENDING;

        sb.append(" ORDER BY ")
                .append(orderBy.getTableColumn())
                .append(" ")
                .append(dirToUse.getQueryString());

        return sb;
    }
    private Query createNativeQuery(final StringBuilder nativeQuerySb, final List<Object> params){
        System.out.println(nativeQuerySb);

        final Query nativeQuery = em.createNativeQuery(nativeQuerySb.toString());

        int i=1;
        for(Object param : params){
            nativeQuery.setParameter(i++, param);
        }

        return nativeQuery;
    }

    @Override
    public List<Review> reviewSearch(
            final User currentUser,
            final String subjectId,
            final Long userId,
            final int page,
            final ReviewOrderField orderBy,
            final OrderDir dir
    ){
        final StringBuilder nativeQuerySb = new StringBuilder("SELECT id FROM reviews WHERE " );
        final List<Object> params = new ArrayList<>();

        final Query nativeQuery = createNativeQuery(appendOrderSql(addSearchFilters(nativeQuerySb, params, currentUser, subjectId, userId),orderBy, dir), params);

        @SuppressWarnings("unchecked")
        final List<Long> ids = (List<Long>) nativeQuery.setFirstResult((page - 1) * PAGE_SIZE)
                .setMaxResults(PAGE_SIZE)
                .getResultList().stream().map(n -> ((Number)n).longValue()).collect(Collectors.toList());

        if(ids.isEmpty()) return Collections.emptyList();

        final StringBuilder hqlQuerySb = new StringBuilder("from Review where id in :ids");
        appendOrderHql(hqlQuerySb, orderBy, dir);

        return em.createQuery(hqlQuerySb.toString(), Review.class)
                .setParameter("ids", ids)
                .getResultList();
    }
    @Override
    public int reviewSearchTotalPages(
            final User currentUser,
            final String subjectId,
            final Long userId
    ){
        final StringBuilder nativeQuerySb = new StringBuilder("SELECT count(*) FROM reviews WHERE " );
        final List<Object> params = new ArrayList<>();

        final Query nativeQuery = createNativeQuery(addSearchFilters(nativeQuerySb, params, currentUser, subjectId, userId), params);

        return (int) Math.max(1, Math.ceil(((Number) nativeQuery.getSingleResult()).doubleValue() / PAGE_SIZE));
    }

    private void appendOrderHql(final StringBuilder sb, final ReviewOrderField orderBy, final OrderDir dir) {
        if (orderBy == null) return;

        OrderDir dirToUse = dir;
        if (dir == null) dirToUse = OrderDir.ASCENDING;

        sb.append(" order by ")
                .append(orderBy.getFieldName())
                .append(" ")
                .append(dirToUse.getQueryString());
    }
}
