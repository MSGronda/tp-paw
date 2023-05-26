package ar.edu.itba.paw.models.enums;

public enum OrderDir {
    ASCENDING("asc"), DESCENDING("desc");

    private final String queryString;

    OrderDir(String queryString) {
        this.queryString = queryString;
    }

    public static OrderDir fromString(String dir) {
        if (dir.equals("desc")) {
            return DESCENDING;
        }
        return ASCENDING;
    }

    public String getQueryString() {
        return queryString;
    }
}
