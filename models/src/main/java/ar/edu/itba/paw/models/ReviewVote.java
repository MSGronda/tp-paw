package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.enums.ReviewVoteType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "reviewvote")
public class ReviewVote {
    @EmbeddedId
    private Key key;

    @ManyToOne(optional = false)
    @MapsId("userId")
    @JoinColumn(name = "iduser")
    private User user;

    @ManyToOne(optional = false)
    @MapsId("reviewId")
    @JoinColumn(name = "idreview")
    private Review review;

    @Column(nullable = false)
    private ReviewVoteType vote;

    protected ReviewVote() {}

    public ReviewVote(final User user, final Review review, final ReviewVoteType vote) {
        this.user = user;
        this.review = review;
        this.vote = vote;
        this.key = new Key();
        this.key.userId = user.getId();
        this.key.reviewId = review.getId();
    }

    public User getUser() {
        return user;
    }

    public Review getReview() {
        return review;
    }

    public ReviewVoteType getVote() {
        return vote;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public void setVote(ReviewVoteType vote) {
        this.vote = vote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReviewVote)) return false;
        ReviewVote that = (ReviewVote) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Embeddable
    private static class Key implements Serializable {
        @Column(name = "idreview")
        private long reviewId;

        @Column(name = "iduser")
        private long userId;

        public long getReviewId() {
            return reviewId;
        }

        public long getUserId() {
            return userId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return reviewId == key.reviewId && userId == key.userId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(reviewId, userId);
        }
    }
}
