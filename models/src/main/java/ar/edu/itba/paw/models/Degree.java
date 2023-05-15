package ar.edu.itba.paw.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Degree {
    private final long id;
    private final String name;
    private final List<Semester> semesters;

    public Degree(final long id,final String name, final List<Semester> semesters) {
        this.id = id;
        this.name = name;
        this.semesters = semesters;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Semester> getSemesters() {
        return new ArrayList<>(semesters);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Degree degree = (Degree) o;
        return id == degree.id && Objects.equals(name, degree.name) && Objects.equals(semesters, degree.semesters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
