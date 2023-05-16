package ar.edu.itba.paw.models;

import java.util.*;

public class Subject {
    private final String id;
    private final String name;
    private final String department;
    private final Integer credits;
    private final Set<String> prerequisites;
    private final Set<Long> professorIds;
    private final Set<Long> degreeIds;
    private final Map<String, SubjectClass> subjectClasses;

    public Subject(
        final String id,
        final String name,
        final String department,
        final int credits,
        final Set<String> prerequisites,
        final Set<Long> professorIds,
        final Set<Long> degreeIds
    ) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.prerequisites = prerequisites;
        this.professorIds = professorIds;
        this.degreeIds = degreeIds;
        this.credits = credits;
        this.subjectClasses = new LinkedHashMap<>();
    }

    public Subject(final String id, final String name, final String department, final int credits) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.credits = credits;
        this.prerequisites = new LinkedHashSet<>();
        this.professorIds = new LinkedHashSet<>();
        this.degreeIds = new LinkedHashSet<>();
        this.subjectClasses = new LinkedHashMap<>();
    }

    public Map<String, SubjectClass> getSubjectClasses() {
        return subjectClasses;
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

    public Integer getCredits() {
        return credits;
    }

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
