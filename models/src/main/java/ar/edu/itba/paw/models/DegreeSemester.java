package ar.edu.itba.paw.models;

import java.util.List;

public class DegreeSemester {
    private final int number;
    private final List<Subject> subjects;

    public DegreeSemester(final int number, final List<Subject> subjects) {
        this.number = number;
        this.subjects = subjects;
    }

    public int getNumber() {
        return number;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }
}
