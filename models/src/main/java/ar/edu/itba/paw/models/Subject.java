package ar.edu.itba.paw.models;

import java.util.*;

public class Subject {
    private final String id;
    private final String name;
    private final String department;
    private final Integer credits;
    private Set<String> prerequisites;
    private Set<Long> professorIds;
    private Set<Long> degreeIds;

    public Subject(String id, String name, String department, Set<String> prerequisites, Set<Long> professorIds, Set<Long> degreeIds, int credits) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.prerequisites = prerequisites;
        this.professorIds = professorIds;
        this.degreeIds = degreeIds;
        this.credits = credits;
    }

    public Subject(String id, String name, String department, int credits) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.credits = credits;
        this.prerequisites = new HashSet<>();
        this.professorIds = new HashSet<>();
        this.degreeIds = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public Set<String> getPrerequisites() {
        return prerequisites;
    }

    public Set<Long> getProfessorIds() {
        return professorIds;
    }

    public Set<Long> getDegreeIds() {
        return degreeIds;
    }

    public Integer getCredits(){ return credits; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(id, subject.getId()) && Objects.equals(name, subject.getName()) && Objects.equals(department, subject.getDepartment()) && Objects.equals(credits, subject.getCredits()) && Objects.equals(prerequisites, subject.getPrerequisites()) && Objects.equals(professorIds, subject.getProfessorIds()) && Objects.equals(degreeIds, subject.getDegreeIds());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
