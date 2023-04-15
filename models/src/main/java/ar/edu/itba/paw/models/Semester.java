package ar.edu.itba.paw.models;

import java.util.List;

public class Semester {
    private final int number;
    private final long degreeId;
    private final List<String> subjectIds;

    public Semester(int number, long degreeId, List<String> subjects) {
        this.number = number;
        this.degreeId = degreeId;
        this.subjectIds = subjects;
    }

    public int getNumber() {
        return number;
    }

    public List<String> getSubjectIds() {
        return subjectIds;
    }

    public long getDegreeId() {
        return degreeId;
    }
}
