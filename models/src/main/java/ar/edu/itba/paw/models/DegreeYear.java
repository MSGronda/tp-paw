package ar.edu.itba.paw.models;

import java.util.List;
import java.util.Objects;

public class DegreeYear {
    private final Degree degree;
    private final int number;
    private final List<Subject> subjects;
    private final int totalCredits;

    public DegreeYear(final Degree degree, final int number, final List<Subject> subjects) {
        this.degree = degree;
        this.number = number;
        this.subjects = subjects;
        int credits = 0;
        for(Subject s: subjects){
            credits += s.getCredits();
        }
        this.totalCredits = credits;
    }

    public int getTotalCredits(){
        return totalCredits;
    }

    public Degree getDegree() {
        return degree;
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
        DegreeYear that = (DegreeYear) o;
        return number == that.number && Objects.equals(degree, that.degree) && Objects.equals(subjects, that.subjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(degree, number);
    }
}
