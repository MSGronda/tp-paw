package ar.edu.itba.paw.webapp.dto;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.User;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class ReviewDto {
    private Long id;
    private String subjectId;
    private Long userId;
    private String difficulty;
    private String timeDemand;
    private String text;
    private Boolean anonymous;
    private Long upVotes;
    private Long downVotes;

    private URI votes;

    public static ReviewDto fromReview(final UriInfo uriInfo, final User currentUser, final Review review){
        final ReviewDto reviewDto =  new ReviewDto();

        reviewDto.id = review.getId();
        reviewDto.subjectId = review.getSubject().getId();
        reviewDto.difficulty = review.getDifficulty().name();
        reviewDto.timeDemand = review.getTimeDemanding().name();
        reviewDto.text = review.getText();
        reviewDto.anonymous = review.isAnonymous();
        reviewDto.upVotes = review.getUpvotes();
        reviewDto.downVotes = review.getDownvotes();

        if(!reviewDto.anonymous || (review.getUser().equals(currentUser))){
            reviewDto.userId = review.getUser().getId();
        }

        reviewDto.votes = uriInfo.getBaseUriBuilder().path("reviews").path(String.valueOf(reviewDto.id)).path("votes").build();

        return reviewDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getTimeDemand() {
        return timeDemand;
    }

    public void setTimeDemand(String timeDemand) {
        this.timeDemand = timeDemand;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }

    public Long getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(Long upVotes) {
        this.upVotes = upVotes;
    }

    public Long getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(Long downVotes) {
        this.downVotes = downVotes;
    }

    public URI getVotes() {
        return votes;
    }

    public void setVotes(URI votes) {
        this.votes = votes;
    }
}
