package ar.edu.itba.paw.models;

import java.util.List;


public class Degree {
    private final long id;
    private final String name;
    private final List<Long> subjectIds;

    public Degree(long id, String name, List<Long> subjectIds) {
        this.id = id;
        this.name = name;
        this.subjectIds = subjectIds;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Long> getSubjectIds() {
        return subjectIds;
    }
}
