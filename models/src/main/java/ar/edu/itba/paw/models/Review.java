package ar.edu.itba.paw.models;

public class Review {
    private final int id;
    private final int userId;
    private final int matId;
    private final String text;

    // Etc.


    public Review(int id, int userId, int matId, String text) {
        this.id = id;
        this.userId = userId;
        this.matId = matId;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getMatId() {
        return matId;
    }

    public String getText() {
        return text;
    }
}
