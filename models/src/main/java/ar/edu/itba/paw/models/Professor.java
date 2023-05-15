package ar.edu.itba.paw.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Professor {
    private final long id;
    private final String name;
    private List<String> subjectIds;

    public Professor(final long id, final String name){
        this.id = id;
        this.name = name;
        this.subjectIds = new ArrayList<>();
    }
    public Professor(final long id, final String name, final List<String> subjectIds) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Professor professor = (Professor) o;
        return id == professor.id && Objects.equals(name, professor.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
