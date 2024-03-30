package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewVote;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.OrderDir;
import ar.edu.itba.paw.models.enums.ReviewOrderField;
import ar.edu.itba.paw.models.enums.ReviewVoteType;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    @Override
    public List<Review> getAllSubjectReviews(
            final Subject subject,
            final int page,
            final ReviewOrderField orderBy,
            final OrderDir dir
    ){
        final StringBuilder nativeQuerySb = new StringBuilder("SELECT id FROM reviews WHERE idsub = ?");
        appendOrderSql(nativeQuerySb, orderBy, dir);

        @SuppressWarnings("unchecked")
        final List<Long> ids = (List<Long>) em.createNativeQuery(nativeQuerySb.toString())
                .setParameter(1, subject.getId())
                .setFirstResult((page - 1) * PAGE_SIZE)
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
    public List<Review> getAllUserReviews(
            final User user,
            final int page,
            final ReviewOrderField orderBy,
            final OrderDir dir
    ){
        final StringBuilder nativeQuerySb = new StringBuilder("SELECT id FROM reviews WHERE iduser = ?");
        appendOrderSql(nativeQuerySb, orderBy, dir);

        @SuppressWarnings("unchecked")
        final List<Long> ids = (List<Long>) em.createNativeQuery(nativeQuerySb.toString())
                .setParameter(1, user.getId())
                .setFirstResult((page - 1) * PAGE_SIZE)
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
    public int getTotalPagesForUserReviews(final User user) {
        final int totalReviews = ((Number) em.createQuery("select count(*) from Review where user = :user")
                .setParameter("user", user)
                .getSingleResult()).intValue();

        return (int) Math.max(1, Math.ceil((double) totalReviews / PAGE_SIZE));
    }

    @Override
    public int getTotalPagesForSubjectReviews(final Subject subject) {
        final int totalReviews = ((Number) em.createQuery("select count(*) from Review where subject = :subject")
                .setParameter("subject", subject)
                .getSingleResult()).intValue();

        return (int) Math.max(1, Math.ceil((double) totalReviews / PAGE_SIZE));
    }

    private void appendOrderSql(final StringBuilder sb, final ReviewOrderField orderBy, final OrderDir dir) {
        if (orderBy == null) return;

        OrderDir dirToUse = dir;
        if (dir == null) dirToUse = OrderDir.ASCENDING;

        sb.append(" ORDER BY ")
                .append(orderBy.getTableColumn())
                .append(" ")
                .append(dirToUse.getQueryString());
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

    @Override
    public List<Review> getReviewFromSubjectAndUser(final Subject subject, final User user) {
        return em.createQuery("from Review where subject = :subject and user = :user", Review.class)
                .setParameter("subject", subject)
                .setParameter("user", user)
                .getResultList();
    }
}
