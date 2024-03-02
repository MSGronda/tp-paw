package ar.edu.itba.paw.persistence.mock;

import ar.edu.itba.paw.models.User;

public class UserMockData {
    public static final long USER1_ID = 1;
    public static final String USER1_EMAIL = "invalid@mail.com";
    public static final String USER1_PASSWORD = "aaaa";
    public static final String USER1_USERNAME = "Test User";
    public static final long USER1_DEGREEID = 1;

    public static final long USER2_ID = 2;
    public static final String USER2_EMAIL = "invalid2@mail.com";
    public static final String USER2_PASSWORD = "bbbb";
    public static final String USER2_USERNAME = "Test User 2";
    public static final long USER2_DEGREEID = 1;


    public static User getUser1() {
        return User.builder().id(USER1_ID).email(USER1_EMAIL).password(USER1_PASSWORD).username(USER1_USERNAME).degree(DegreeMockData.getDegree1()).build();
    }

    public static User getUser2() {
        return User.builder().id(USER2_ID).email(USER2_EMAIL).password(USER2_PASSWORD).username(USER2_USERNAME).degree(DegreeMockData.getDegree1()).build();
    }




}
