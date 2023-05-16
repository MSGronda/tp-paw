package ar.edu.itba.paw.models;

public class Review {
    private static final int PREVIEW_SIZE=500;

    private final long id;
    private final long userId;
    private final String subjectId;
    private final int easy;
    private final int timeDemanding;
    private final String text;
    private final String subjectName;
    private final long upvotes;
    private final long downvotes;
    private final String username;
    private final boolean anonymous;
    private final String previewText;
    private final String showMoreText;
    private final boolean requiresShowMore;

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

        //TODO: Esto tiene que ir en los controllers en webapp, no en models.
        // Es detalle de implementación del frontend.
        if(text.length() > PREVIEW_SIZE){
            this.requiresShowMore = true;
            this.previewText = this.text.substring(0,PREVIEW_SIZE);
            this.showMoreText = this.text.substring(PREVIEW_SIZE);
        } else {
            this.requiresShowMore = false;
            this.previewText = "";
            this.showMoreText = "";
        }
    }

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

    public String getPreviewText() {
        return previewText;
    }

    public Boolean getRequiresShowMore() {
        return requiresShowMore;
    }

    public String getShowMoreText() {
        return showMoreText;
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
}
