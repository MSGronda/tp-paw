package ar.edu.itba.paw.models;

public class Review {
    private final long id;
    private final long userId;
    private final String subjectId;
    private Integer easy;
    private Integer timeDemanding;
    private String text;

    private String subjectName = null;

    private Integer upvotes = 0;
    private Integer downvotes = 0;

    private String username = null;
    private Boolean anonymous;

    private final String previewText;

    private final String showMoreText;

    private final Boolean requiresShowMore;

    private final int PREVIEW_SIZE=500;


    public Review(final long id, final long userId, final String subjectId, final Integer easy,
                  final Integer timeDemanding, final String text, final Boolean anonymous) {
        this.id = id;
        this.userId = userId;
        this.subjectId = subjectId;
        this.easy = easy;
        this.timeDemanding = timeDemanding;
        this.text = text;
        this.anonymous = anonymous;
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

    public Review(final long id, final long userId, final String subjectId, final Integer easy, final Integer timeDemanding,
                  final String text, final String subjectName, final int upvotes, final int downvotes, final Boolean anonymous) {
        this.id = id;
        this.userId = userId;
        this.subjectId = subjectId;
        this.easy = easy;
        this.timeDemanding = timeDemanding;
        this.text = text;
        this.subjectName=subjectName;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.anonymous = anonymous;
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
    public Review(final long id, final long userId, final String username, final String subjectId, final Integer easy,
                  final Integer timeDemanding, final String text, final int upvotes, final int downvotes, final Boolean anonymous) {
        this.id = id;
        this.userId = userId;
        this.subjectId = subjectId;
        this.easy = easy;
        this.timeDemanding = timeDemanding;
        this.text = text;
        this.username=username;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.anonymous = anonymous;
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

    public int getUpvotes(){
        return upvotes;
    }
    public int getDownvotes(){
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

    public Integer getEasy(){
    return easy;
    }

    public Integer getTimeDemanding(){
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

    public void setText(final String text){
        this.text = text;
    }

    public void setEasy(final Integer easy){
        this.easy = easy;
    }

    public void setTimeDemanding(final Integer timeDemanding){
        this.timeDemanding = timeDemanding;
    }

    public void setAnonymous(final Boolean anonymous){
        this.anonymous = anonymous;
    }
}


