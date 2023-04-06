package ar.edu.itba.paw.models;

import java.util.List;

public class Professor {
    private final long id;
    private final String name;
    private List<Long> subjectIds;

    public Professor(long id, String name){
        this.id = id;
        this.name = name;
    }
    public Professor(long id, String name, List<Long> subjectIds) {
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
