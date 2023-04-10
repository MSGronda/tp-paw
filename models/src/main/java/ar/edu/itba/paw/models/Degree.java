package ar.edu.itba.paw.models;

import java.util.List;
import java.util.Objects;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Degree degree = (Degree) o;
        return id == degree.id && name.equals(degree.name) && subjectIds.equals(degree.subjectIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
