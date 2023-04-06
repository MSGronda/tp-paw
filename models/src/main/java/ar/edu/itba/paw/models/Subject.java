package ar.edu.itba.paw.models;

import java.util.List;

public class Subject {
    private final long id;
    private final String name;
    private final String department;
    private final Integer credits;
    private List<Long> prerequisites;
    private List<Long> professorIds;
    private List<Long> degreeIds;

    public Subject(long id, String name, String department, List<Long> prerequisites, List<Long> professorIds, List<Long> degreeIds, int credits) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.prerequisites = prerequisites;
        this.professorIds = professorIds;
        this.degreeIds = degreeIds;
        this.credits = credits;
    }

    public Subject(long id, String name, String department, int credits) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.credits = credits;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public List<Long> getPrerequisites() {
        return prerequisites;
    }

    public List<Long> getProfessorIds() {
        return professorIds;
    }

    public List<Long> getDegreeIds() {
        return degreeIds;
    }

    public Integer getCredits(){ return credits; }
}
