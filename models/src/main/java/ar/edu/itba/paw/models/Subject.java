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

    private Subject(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.department = builder.department;
        this.credits = builder.credits;
        this.prerequisites = builder.prerequisites;
        this.professorIds = builder.professorIds;
        this.degreeIds = builder.degreeIds;
        this.subjectClasses = builder.subjectClasses;
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

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builderFrom(Subject subject) {
        return new Builder(subject);
    }


    public static class Builder {
        private String id;
        private String name;
        private String department;
        private Integer credits;
        private Set<String> prerequisites;
        private Set<Long> professorIds;
        private Set<Long> degreeIds;
        private Map<String, SubjectClass> subjectClasses;

        private Builder() {
            this.prerequisites = new LinkedHashSet<>();
            this.professorIds = new LinkedHashSet<>();
            this.degreeIds = new LinkedHashSet<>();
            this.subjectClasses = new LinkedHashMap<>();
        }

        private Builder(Subject subject) {
            this.id = subject.id;
            this.name = subject.name;
            this.department = subject.department;
            this.credits = subject.credits;
            this.prerequisites = subject.prerequisites;
            this.professorIds = subject.professorIds;
            this.degreeIds = subject.degreeIds;
            this.subjectClasses = subject.subjectClasses;
        }

        public Builder id(final String id) {
            this.id = id;
            return this;
        }

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder department(final String department) {
            this.department = department;
            return this;
        }

        public Builder credits(final Integer credits) {
            this.credits = credits;
            return this;
        }

        public Builder prerequisites(final Set<String> prerequisites) {
            this.prerequisites = prerequisites;
            return this;
        }

        public Builder professorIds(final Set<Long> professorIds) {
            this.professorIds = professorIds;
            return this;
        }

        public Builder degreeIds(final Set<Long> degreeIds) {
            this.degreeIds = degreeIds;
            return this;
        }

        public Builder subjectClasses(final Map<String, SubjectClass> subjectClasses) {
            this.subjectClasses = subjectClasses;
            return this;
        }

        public Subject build() {
            return new Subject(this);
        }
    }
}
