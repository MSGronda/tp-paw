package ar.edu.itba.paw.persistence.mock;

import ar.edu.itba.paw.models.Degree;

public class DegreeMockData {
    public static final long DEG1_ID = 1;
    public static final String DEG1_NAME = "Ing. Informatica";
    public static final int DEG1_CREDITS = 240;

    public static final long DEG2_ID = 2;
    public static final String DEG2_NAME = "Ing. Mecanica";
    public static final int DEG2_CREDITS = 240;

    public static Degree getDegree1() {
        return Degree.builder().id(DEG1_ID).name(DEG1_NAME).totalCredits(DEG1_CREDITS).build();
    }
    public static Degree getDegree2() {
        return Degree.builder().id(DEG2_ID).name(DEG2_NAME).totalCredits(DEG2_CREDITS).build();
    }
}
