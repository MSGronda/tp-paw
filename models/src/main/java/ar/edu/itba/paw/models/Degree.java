package ar.edu.itba.paw.models;

import java.util.List;

public class Degree {
    private final int id;
    private final String name;
    private final List<Integer> subjectIds;

    public Degree(int id, String name, List<Integer> subjectIds) {
        this.id = id;
        this.name = name;
        this.subjectIds = subjectIds;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getSubjectIds() {
        return subjectIds;
    }
}
