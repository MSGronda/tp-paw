package ar.edu.itba.paw.webapp.dto;
import ar.edu.itba.paw.models.Review;
import javax.ws.rs.core.UriInfo;

public class ReviewDto {
    private Long id;
    private String subjectId;
    private String difficulty;
    private String timeDemand;
    private String text;
    private Boolean anonymous;
    private Long upVotes;
    private Long downVotes;

    public static ReviewDto fromReview(UriInfo uriInfo, Review review){
        final ReviewDto reviewDto =  new ReviewDto();

        reviewDto.id = review.getId();
        reviewDto.subjectId = review.getSubject().getId();
        reviewDto.difficulty = review.getDifficulty().name();
        reviewDto.timeDemand = review.getTimeDemanding().name();
        reviewDto.text = review.getText();
        reviewDto.anonymous = review.isAnonymous();
        reviewDto.upVotes = review.getUpvotes();
        reviewDto.downVotes = review.getDownvotes();

        // TODO: list de ReviewVotes

        return reviewDto;
    }

    // Getters

    public Long getId() {
        return id;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getTimeDemand() {
        return timeDemand;
    }

    public String getText() {
        return text;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public Long getUpVotes() {
        return upVotes;
    }

    public Long getDownVotes() {
        return downVotes;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setTimeDemand(String timeDemand) {
        this.timeDemand = timeDemand;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }

    public void setUpVotes(Long upVotes) {
        this.upVotes = upVotes;
    }

    public void setDownVotes(Long downVotes) {
        this.downVotes = downVotes;
    }
}
