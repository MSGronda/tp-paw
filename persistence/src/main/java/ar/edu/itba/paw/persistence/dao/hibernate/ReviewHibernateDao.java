package ar.edu.itba.paw.persistence.dao.hibernate;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewStats;
import ar.edu.itba.paw.persistence.constants.Tables;
import ar.edu.itba.paw.persistence.constants.Views;
import ar.edu.itba.paw.persistence.dao.ReviewDao;
import ar.edu.itba.paw.persistence.dao.jdbc.ReviewJdbcDao;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ReviewHibernateDao implements ReviewDao {

    @PersistenceContext
    private EntityManager em;
    private static final Integer PAGE_SIZE = 10;

    @Override
    public Review create(final Review review) throws SQLException{
        em.persist(review);
        return review;
    }

    public Integer deleteReviewVoteByReviewId(final Long idReview){
        return em.createNativeQuery("delete from " + Tables.REVIEW_VOTES + " where idReview = :idReview")
                .setParameter("idReview",idReview).getFirstResult();
    }
    public Integer deleteReviewVote(final Long idUser, final Long idReview){
        return em.createNativeQuery("delete from " + Tables.REVIEW_VOTES + " where idReview = :idReview and idUser = :idUser")
                .setParameter("idReview",idReview).setParameter("idUser",idUser).getFirstResult();
    }
    public Integer voteReview(final Long idUser, final Long idReview, final int vote){
        return em.createNativeQuery("INSERT INTO " + Tables.REVIEW_VOTES+ "  (idReview, idUser, vote) VALUES (:idReview, :idUser , :vote)")
                .setParameter("idReview",idReview).setParameter("idUser",idUser).setParameter("vote",vote).getFirstResult();
    }
    public Integer updateVoteOnReview(final Long idUser, final Long idReview, final int vote){
        return em.createNativeQuery("UPDATE " + Tables.REVIEW_VOTES + " SET vote = :vote WHERE idreview = :idReview AND iduser = :idUser")
                .setParameter("vote",vote).setParameter("idReview",idReview).setParameter("idUser",idUser).getFirstResult();
    }
    public boolean userVotedOnReview(final Long idUser, final Long idReview){
        return em.createNativeQuery("SELECT * FROM " + Tables.REVIEW_VOTES + " WHERE idUser = :idUser AND idReview = :idReview")
                .setParameter("idUser",idUser).setParameter("idReview",idReview).getResultList().isEmpty();
    }
    // key: idReview - value: vote
    public Map<Long,Integer> userReviewVoteByIdSubAndIdUser(final String idSub, final Long idUser){
        Query q = em.createNativeQuery("SELECT rv.idReview, rv.vote FROM " + Tables.REVIEW_VOTES + " AS rv FULL JOIN " + Tables.REVIEWS +
                "AS r ON rv.idReview = r.id WHERE r.idSub = :idSub AND rv.idUser = :idUser")
                .setParameter("idSub",idSub).setParameter("idUser",idUser);
        Map<Long,Integer> toReturn = new HashMap<>();
        List<Object[]> resultList = q.getResultList();
        for(Object[] item: resultList){
            toReturn.put( (Long) item[0], (Integer) item[1]);
        }
        return toReturn;
    }

    // key: idReview - value: vote
    public Map<Long,Integer> userReviewVoteByIdUser(final Long idUser){
        Query q = em.createNativeQuery("SELECT idReview, vote FROM " + Tables.REVIEW_VOTES + " WHERE  idUser = :idUser")
                .setParameter("idUser",idUser);
        Map<Long,Integer> toReturn = new HashMap<>();
        List<Object[]> resultList = q.getResultList();
        for(Object[] item: resultList){
            toReturn.put( (Long) item[0], (Integer) item[1]);
        }
        return toReturn;
    }
    public List<ReviewStats> getReviewStatListByDegreeId(final long id){
        Query q = em.createNativeQuery("SELECT * FROM " + Tables.SUBJECTS_DEGREES + " AS sg FULL JOIN " + Views.REVIEW_STATS +
                "AS rs ON rs.idsub = sg.idsub WHERE  sg.iddeg = :iddeg ")
                .setParameter("iddeg",id);
    }
    public List<Review> getAllBySubject(final String id){
        return em.createQuery("FROM Review review WHERE review.subjectId = :id",Review.class)
                .setParameter("id",id).getResultList();
    }
    public Map<String, ReviewStats> getReviewStatMapBySubjectList(final List<String> idSubs){}
    public Optional<ReviewStats> getReviewStatBySubject(final String idSub){}
    public List<ReviewStats> getReviewStatBySubjectList(final List<String> idSubs){}
    public List<Review> getAllUserReviewsWithSubjectName(final Long userId,final Map<String, String> params){
        String orderBy = " ORDER BY " + params.getOrDefault("order", "semester") + " " +
                params.getOrDefault("dir", "DESC");
        Query nativeQuery = em.createNativeQuery("SELECT id FROM reviews WHERE iduser = :userId" + orderBy)
                .setParameter("userId",userId);
        nativeQuery.setMaxResults(PAGE_SIZE);
        int pageNum = Integer.parseInt(params.getOrDefault("pageNum","0"));
        nativeQuery.setFirstResult(pageNum*PAGE_SIZE);

        @SuppressWarnings("unchecked")
        final List<Long> idList = (List<Long>) nativeQuery.getResultList().stream()
                .map(n -> (Long) ((Number) n).longValue() ).collect(Collectors.toList());
        final TypedQuery<Review> query = em.createQuery("from Review WHERE id IN :ids",Review.class);
        query.setParameter("ids",idList);

        return query.getResultList();
    }
    public int getTotalPagesFromUserReviews(final Long userId){
        Query totalPagesQuery = em.createQuery("select Count(id) FROM Review WHERE iduser = :userId")
                .setParameter("userId",userId);
        return totalPagesQuery.getFirstResult() % PAGE_SIZE == 0? (totalPagesQuery.getFirstResult() / PAGE_SIZE) - 1: (totalPagesQuery.getFirstResult() / PAGE_SIZE);
    }
    public int getTotalPagesForReviews(final String subjectId){
        Query totalPagesQuery = em.createQuery("select Count(id) FROM Review WHERE idsub = :subjectId")
                .setParameter("subjectId",subjectId);
        return totalPagesQuery.getFirstResult() % PAGE_SIZE == 0? (totalPagesQuery.getFirstResult() / PAGE_SIZE) - 1: (totalPagesQuery.getFirstResult() / PAGE_SIZE);
    }
    public List<Review> getAllSubjectReviewsWithUsername(final String subjectId, final Map<String,String> params){
        String orderBy = " ORDER BY " + params.getOrDefault("order", "easy") + " " +
                params.getOrDefault("dir", "ASC");
        Query nativeQuery = em.createNativeQuery("SELECT id FROM reviews WHERE idsub = :subjectId" + orderBy)
                .setParameter("subjectId",subjectId);
        nativeQuery.setMaxResults(PAGE_SIZE);
        int pageNum = Integer.parseInt(params.getOrDefault("pageNum","0"));
        nativeQuery.setFirstResult(pageNum*PAGE_SIZE);

        @SuppressWarnings("unchecked")
        final List<Long> idList = (List<Long>) nativeQuery.getResultList().stream()
                .map(n -> (Long) ((Number) n).longValue() ).collect(Collectors.toList());
        final TypedQuery<Review> query = em.createQuery("from Review WHERE id IN :ids",Review.class);
        query.setParameter("ids",idList);

        return query.getResultList();
    }
    public Boolean didUserReviewDB(final String subjectId, final Long userId){
        final List<Review> r = em.createQuery("from Review where idsub = :subjectId and iduser = :userid")
                .setParameter("subjectId",subjectId).setParameter("userid",userId).getResultList();
        return r.isEmpty();
    }

    public void update(final Review review){
        final Review r = em.find(Review.class,review.getId());
        r.setEasy(review.getEasy());
        r.setTimeDemanding(review.getTimeDemanding());
        r.setText(review.getText());
        r.setAnonymous(review.getAnonymous());
        em.merge(r);
    }
}
