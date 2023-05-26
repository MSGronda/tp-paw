package ar.edu.itba.paw.models;

import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_id_seq")
    @SequenceGenerator(sequenceName = "review_id_seq", name = "review_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "iduser")
    @Column(name = "iduser", nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "idsub")
    @Column(name = "idsub",nullable = false)
    private String subjectId;

    @Column
    private Integer easy;

    @Column
    private Integer timeDemanding;

    @Column(name = "revtext", columnDefinition = "TEXT",nullable = false)
    private String text;

    @Column(name = "useranonymous")
    private Boolean anonymous;

    @ManyToOne
    @JoinColumn(name = "subname")
    private String subjectName;

    @ManyToOne
    @JoinTable(
            name = "users",
            joinColumns = @JoinColumn(name = "iduser")
    )
    private String username;

    @Formula("SELECT COUNT(vote) FROM review_vote WHERE id = :id AND vote = 1")
    private Long upvotes;

    @Formula("SELECT COUNT(vote) FROM review_vote WHERE id = :id AND vote = -1")
    private Long downvotes;

    private Review(Builder builder) {
        this.id = builder.id;
        this.userId = builder.userId;
        this.subjectId = builder.subjectId;
        this.easy = builder.easy;
        this.timeDemanding = builder.timeDemanding;
        this.text = builder.text;
        this.anonymous = builder.anonymous;
        this.subjectName = builder.subjectName;
        this.upvotes = builder.upvotes;
        this.downvotes = builder.downvotes;
        this.username = builder.username;
    }

    Review() {}

    public long getUpvotes(){
        return upvotes;
    }
    public long getDownvotes(){
        return downvotes;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public String getText() {
        return text;
    }

    public int getEasy(){
    return easy;
    }

    public int getTimeDemanding(){
        return timeDemanding;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getUsername(){
        return username;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setEasy(Integer easy) {
        this.easy = easy;
    }

    public void setTimeDemanding(Integer timeDemanding) {
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

    public static class Builder {
        private long id;
        private long userId;
        private String subjectId;
        private int easy;
        private int timeDemanding;
        private String text;
        private String subjectName;
        private long upvotes;
        private long downvotes;
        private String username;
        private boolean anonymous;

        private Builder() {

        }

        private Builder(Review review) {
            this.id = review.id;
            this.userId = review.userId;
            this.subjectId = review.subjectId;
            this.easy = review.easy;
            this.timeDemanding = review.timeDemanding;
            this.text = review.text;
            this.subjectName = review.subjectName;
            this.upvotes = review.upvotes;
            this.downvotes = review.downvotes;
            this.username = review.username;
            this.anonymous = review.anonymous;
        }

        public Builder id(final long id) {
            this.id = id;
            return this;
        }

        public Builder userId(final long userId) {
            this.userId = userId;
            return this;
        }

        public Builder subjectId(final String subjectId) {
            this.subjectId = subjectId;
            return this;
        }

        public Builder easy(final int easy) {
            this.easy = easy;
            return this;
        }

        public Builder timeDemanding(final int timeDemanding) {
            this.timeDemanding = timeDemanding;
            return this;
        }

        public Builder text(final String text) {
            this.text = text;
            return this;
        }

        public Builder subjectName(final String subjectName) {
            this.subjectName = subjectName;
            return this;
        }

        public Builder upvotes(final long upvotes) {
            this.upvotes = upvotes;
            return this;
        }

        public Builder downvotes(final long downvotes) {
            this.downvotes = downvotes;
            return this;
        }

        public Builder username(final String username) {
            this.username = username;
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

    public enum ReviewVote{
        UPVOTE(1),
        DOWNVOTE(-1),
        DELETE(0);

        private int vote;
        ReviewVote(int vote){
            this.vote = vote;
        }

        public int getVote() {
            return vote;
        }
        public static ReviewVote getVoteByNum(int vote){
            for(ReviewVote v : ReviewVote.values()){
                if(v.vote == vote){
                    return v;
                }
            }
            throw new IllegalArgumentException(String.valueOf(vote));
        }
    }
}
