package ar.edu.itba.paw.models;

public class Review {
    private final long id;
    private final long userId;
    private final long matId;
    private final String title;
    private final String text;

    // Etc.


    public Review(long id, long userId, long matId,String title, String text) {
        this.id = id;
        this.userId = userId;
        this.matId = matId;
        this.title = title;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public long getMatId() {
        return matId;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }
}
