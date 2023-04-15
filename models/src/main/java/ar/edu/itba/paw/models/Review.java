package ar.edu.itba.paw.models;

public class Review {
    private final long id;
    private final long userId;
    private final String userEmail;
    private final String subjectId;
    private final Integer easy;
    private final Boolean timeDemanding;
    private final String text;

    // Etc.




    public Review(long id, long userId, String userEmail, String subjectId, Integer easy, Boolean timeDemanding, String text) {
        this.id = id;
        this.userId = userId;
        this.userEmail = userEmail;
        this.subjectId = subjectId;
        this.easy = easy;
        this.timeDemanding = timeDemanding;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }
    public String getUserEmail() {
        return userEmail;
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

    public Boolean getTimeDemanding(){
        return timeDemanding;
    }
}


