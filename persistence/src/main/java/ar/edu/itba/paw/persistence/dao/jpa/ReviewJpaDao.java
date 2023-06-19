package ar.edu.itba.paw.persistence.dao.jpa;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewVote;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.OrderDir;
import ar.edu.itba.paw.models.enums.ReviewOrderField;
import ar.edu.itba.paw.models.enums.ReviewVoteType;
import ar.edu.itba.paw.persistence.dao.ReviewDao;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ReviewJpaDao implements ReviewDao {
    private static final int PAGE_SIZE = 10;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Review create(final Review review) {
        em.persist(review);
        return review;
    }

    @Override
    public Review update(final Review review) {
        final Review r = em.find(Review.class, review.getId());
        r.setAnonymous(review.isAnonymous());
        r.setText(review.getText());
        r.setDifficulty(review.getDifficulty());
        r.setTimeDemanding(review.getTimeDemanding());
        return r;
    }

    @Override
    public void delete(final Review review) {
        em.remove(review);
    }

    @Override
    public Optional<Review> findById(final long id) {
        return Optional.ofNullable(em.find(Review.class, id));
    }

    @Override
    public List<Review> getAll() {
        return em.createQuery("from Review", Review.class)
                .getResultList();
    }

    public Optional<ReviewVote> voteReview(final User user, final Review review, final ReviewVoteType vote){
        Optional<ReviewVote> maybeReviewVote = em.createQuery("from ReviewVote where user = :user and review = :review", ReviewVote.class)
                .setParameter("user", user)
                .setParameter("review", review)
                .getResultList()
                .stream().findFirst();

        if(vote == null) {
            maybeReviewVote.ifPresent(reviewVote -> em.remove(reviewVote));
            return Optional.empty();
        }

        if(maybeReviewVote.isPresent()) {
            final ReviewVote reviewVote = maybeReviewVote.get();
            reviewVote.setVote(vote);
            return Optional.of(reviewVote);
        }

        final ReviewVote reviewVote = new ReviewVote(user, review, vote);
        em.persist(reviewVote);

        return Optional.of(reviewVote);
    }

    public boolean didUserVote(final User user, final Review review) {
        return !em.createQuery("from ReviewVote where user = :user and review = :review", ReviewVote.class)
                .setParameter("user", user)
                .setParameter("review", review)
                .getResultList().isEmpty();
    }

    public boolean didUserReview(final Subject subject, final User user) {
        return !em.createQuery("from Review where subject = :subject and user = :user", Review.class)
                .setParameter("subject", subject)
                .setParameter("user", user)
                .getResultList().isEmpty();
    }

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

    public int getTotalPagesForUserReviews(final User user) {
        final int totalReviews = ((Number) em.createQuery("select count(*) from Review where user = :user")
                .setParameter("user", user)
                .getSingleResult()).intValue();

        return (int) Math.max(1, Math.ceil((double) totalReviews / PAGE_SIZE));
    }

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
}
