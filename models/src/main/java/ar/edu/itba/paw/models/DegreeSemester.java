package ar.edu.itba.paw.models;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DegreeSemester that = (DegreeSemester) o;
        return number == that.number && Objects.equals(subjects, that.subjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, subjects);
    }
}
