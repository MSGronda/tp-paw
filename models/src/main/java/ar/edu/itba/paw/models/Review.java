package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.enums.Difficulty;
import ar.edu.itba.paw.models.enums.TimeDemanding;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "reviews")
public class Review {
    private static final int PREVIEW_LENGTH = 500;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviews_id_seq")
    @SequenceGenerator(sequenceName = "reviews_id_seq", name = "reviews_id_seq", allocationSize = 1)
    private long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "iduser")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "idsub")
    private Subject subject;

    @Column(name = "easy")
    private Difficulty difficulty;

    @Column(name = "timedemanding")
    private TimeDemanding timeDemanding;

    @Column(name = "revtext", columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(name = "useranonymous")
    private boolean anonymous;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewVote> votes;

    @Formula("(SELECT COUNT(*) FROM reviewvote r WHERE r.idreview = id AND r.vote = 1)")
    private long upvotes;

    @Formula("(SELECT COUNT(*) FROM reviewvote r WHERE r.idreview = id AND r.vote = -1)")
    private long downvotes;

    private Review(Builder builder) {
        this.id = builder.id;
        this.user = builder.user;
        this.subject = builder.subject;
        this.difficulty = builder.difficulty;
        this.timeDemanding = builder.timeDemanding;
        this.text = builder.text;
        this.anonymous = builder.anonymous;
    }

    protected Review() {}

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Subject getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public Difficulty getDifficulty(){
    return difficulty;
    }

    public TimeDemanding getTimeDemanding(){
        return timeDemanding;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public List<ReviewVote> getVotes() {
        return votes;
    }

    public long getUpvotes(){
        return upvotes;
    }
    public long getDownvotes(){
        return downvotes;
    }

    public String getPreviewText() {
        return this.text.substring(0, PREVIEW_LENGTH);
    }

    public boolean getRequiresShowMore() {
        return this.text.length() > PREVIEW_LENGTH;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setTimeDemanding(TimeDemanding timeDemanding) {
        this.timeDemanding = timeDemanding;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builderFrom(Review review) {
        return new Builder(review);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review)) return false;
        Review review = (Review) o;
        return getId() == review.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public static class Builder {
        private long id;
        private User user;
        private Subject subject;
        private Difficulty difficulty;
        private TimeDemanding timeDemanding;
        private String text;
        private boolean anonymous;

        private Builder() {

        }

        private Builder(Review review) {
            this.id = review.id;
            this.user = review.user;
            this.subject = review.subject;
            this.difficulty = review.difficulty;
            this.timeDemanding = review.timeDemanding;
            this.text = review.text;
            this.anonymous = review.anonymous;
        }

        public Builder id(final long id) {
            this.id = id;
            return this;
        }

        public Builder user(final User user) {
            this.user = user;
            return this;
        }

        public Builder subject(final Subject subject) {
            this.subject = subject;
            return this;
        }

        public Builder difficulty(final Difficulty difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        public Builder timeDemanding(final TimeDemanding timeDemanding) {
            this.timeDemanding = timeDemanding;
            return this;
        }

        public Builder text(final String text) {
            this.text = text;
            return this;
        }

        public Builder anonymous(final boolean anonymous) {
            this.anonymous = anonymous;
            return this;
        }

        public Review build() {
            return new Review(this);
        }
    }
}
