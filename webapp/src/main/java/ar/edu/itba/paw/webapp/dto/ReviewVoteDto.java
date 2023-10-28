package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.ReviewVote;
import ar.edu.itba.paw.models.Subject;

import javax.ws.rs.core.UriInfo;

public class ReviewVoteDto {
    private Long userId;

    private Long reviewId;

    private Long vote;

    public static ReviewVoteDto fromReviewVote(final UriInfo uriInfo, final ReviewVote reviewVote){
        final ReviewVoteDto reviewVoteDto = new ReviewVoteDto();
        reviewVoteDto.reviewId = reviewVote.getReview().getId();
        reviewVoteDto.userId = reviewVote.getUser().getId();
        reviewVoteDto.vote = reviewVote.getVote().getIntValue();

        return reviewVoteDto;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Long getVote() {
        return vote;
    }

    public void setVote(Long vote) {
        this.vote = vote;
    }
}
