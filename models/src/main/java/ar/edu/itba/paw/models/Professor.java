package ar.edu.itba.paw.models;

import java.util.ArrayList;
import java.util.List;

public class Professor {
    private final long id;
    private final String name;
    private List<String> subjectIds;

    public Professor(long id, String name){
        this.id = id;
        this.name = name;
        this.subjectIds = new ArrayList<>();
    }
    public Professor(long id, String name, List<String> subjectIds) {
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

    public List<String> getSubjectIds() {
        return subjectIds;
    }
}
