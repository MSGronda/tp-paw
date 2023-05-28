package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.enums.ReviewVoteType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class ReviewVoteForm {
    @NotNull
    private int vote;

    @NotNull
    private Long reviewId;

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public ReviewVoteType getVote() {
        return ReviewVoteType.parse(vote);
    }

    public void setVote(int vote) {
        this.vote = vote;
    }
}
