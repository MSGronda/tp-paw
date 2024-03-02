package ar.edu.itba.paw.persistence.mock;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Subject;

public class SubjectMockData {
    public static final String SUB1_ID = "11.15";
    public static final String SUB1_NAME = "Test Subject";
    public static final String SUB1_DEPARTMENT = "Informatica";
    public static final int SUB1_CREDITS = 6;

    public static final String SUB2_ID = "11.16";
    public static final String SUB2_NAME = "Test Subject 2";
    public static final String SUB2_DEPARTMENT = "Informatica";
    public static final int SUB2_CREDITS = 3;

    public static final String SUB3_ID = "11.18";
    public static final String SUB3_NAME = "Test Subject 4";
    public static final String SUB3_DEPARTMENT = "department";
    public static final int SUB3_CREDITS = 5;

    public static Subject getSubject1() {
        return Subject.builder().id(SUB1_ID).name(SUB1_NAME).department(SUB1_DEPARTMENT).credits(SUB1_CREDITS).build();
    }

    public static Subject getSubject2() {
        return Subject.builder().id(SUB2_ID).name(SUB2_NAME).department(SUB2_DEPARTMENT).credits(SUB2_CREDITS).build();
    }

    public static Subject getSubject3() {
        return Subject.builder().id(SUB3_ID).name(SUB3_NAME).department(SUB3_DEPARTMENT).credits(SUB3_CREDITS).build();
    }
}
