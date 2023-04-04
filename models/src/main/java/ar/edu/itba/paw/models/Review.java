package ar.edu.itba.paw.models;

public class Review {
    private final int id;
    private final String userEmail;
    private final int matId;
    private final String text;

    // Etc.


    public Review(int id, String userEmail, int matId, String text) {
        this.id = id;
        this.userEmail = userEmail;
        this.matId = matId;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getUserId() {
        return userEmail;
    }

    public int getMatId() {
        return matId;
    }

    public String getText() {
        return text;
    }
}
