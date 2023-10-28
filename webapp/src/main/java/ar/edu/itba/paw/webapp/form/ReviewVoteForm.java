package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.enums.ReviewVoteType;

import javax.validation.constraints.NotNull;

public class ReviewVoteForm {
    @NotNull
    private Long vote;

    @NotNull
    private Long reviewId;

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(final Long reviewId) {
        this.reviewId = reviewId;
    }

    public ReviewVoteType getVoteType() {
        return ReviewVoteType.parse(vote);
    }

    public Long getVote() {
        return vote;
    }

    public void setVote(final Long vote) {
        this.vote = vote;
    }
}
