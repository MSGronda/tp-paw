package ar.edu.itba.paw.models;

public class Review {
    private final long id;
    private final long userId;
    private final String subjectId;
    private final Integer easy;
    private final Integer timeDemanding;
    private final String text;

    private String subjectName = null;

    private Integer upvotes = 0;
    private Integer downvotes = 0;

    private String username = null;

    // Etc.

    public Review(long id, long userId, String subjectId, Integer easy, Integer timeDemanding, String text) {
        this.id = id;
        this.userId = userId;
        this.subjectId = subjectId;
        this.easy = easy;
        this.timeDemanding = timeDemanding;
        this.text = text;
    }

    public Review(long id, long userId,  String subjectId, Integer easy, Integer timeDemanding,
                  String text, String subjectName, int upvotes, int downvotes) {
        this.id = id;
        this.userId = userId;
        this.subjectId = subjectId;
        this.easy = easy;
        this.timeDemanding = timeDemanding;
        this.text = text;
        this.subjectName=subjectName;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }
    public Review(long id, long userId,String username, String subjectId, Integer easy, Integer timeDemanding,
                  String text, int upvotes, int downvotes) {
        this.id = id;
        this.userId = userId;
        this.subjectId = subjectId;
        this.easy = easy;
        this.timeDemanding = timeDemanding;
        this.text = text;
        this.username=username;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
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
}


