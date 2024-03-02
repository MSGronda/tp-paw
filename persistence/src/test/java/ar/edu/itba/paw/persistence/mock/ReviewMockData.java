package ar.edu.itba.paw.persistence.mock;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.enums.Difficulty;
import ar.edu.itba.paw.models.enums.TimeDemanding;

public class ReviewMockData {
    public static final long REV1_ID = 1;
    public static final long REV1_USERID = UserMockData.USER1_ID;
    public static final String REV1_SUBID = SubjectMockData.SUB1_ID;
    public static final Difficulty REV1_DIFFICULTY = Difficulty.EASY;
    public static final TimeDemanding REV1_TIMEDEMAND = TimeDemanding.LOW;
    public static final String REV1_TEXT = "Very Easy";
    public static final boolean REV1_ANONYMOUS = false;

    public static final long REV2_ID = 2;
    public static final long REV2_USERID = UserMockData.USER2_ID;
    public static final String REV2_SUBID = SubjectMockData.SUB2_ID;
    public static final Difficulty REV2_DIFFICULTY = Difficulty.EASY;
    public static final TimeDemanding REV2_TIMEDEMAND = TimeDemanding.MEDIUM;
    public static final String REV2_TEXT = "Real Easy";
    public static final boolean REV2_ANONYMOUS = false;

    public static Review getReview1() {
        return Review.builder().id(REV1_ID).user(UserMockData.getUser1()).subject(SubjectMockData.getSubject1()).difficulty(REV1_DIFFICULTY).timeDemanding(REV1_TIMEDEMAND).text(REV1_TEXT).anonymous(REV1_ANONYMOUS).build();
    }

    public static Review getReview2() {
        return Review.builder().id(REV2_ID).user(UserMockData.getUser2()).subject(SubjectMockData.getSubject2()).difficulty(REV2_DIFFICULTY).timeDemanding(REV2_TIMEDEMAND).text(REV2_TEXT).anonymous(REV2_ANONYMOUS).build();
    }
}
