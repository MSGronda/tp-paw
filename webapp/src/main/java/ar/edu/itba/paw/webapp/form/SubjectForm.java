package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.*;
import java.util.List;


public class SubjectForm {
    @NotNull
    @Pattern(regexp = "[0-9]{2}\\.[0-9]{2}")
    private String id;
    @Size(min=1, max=50)
    private String name;
    @Size(min=1, max=40)
    private String department;
    @NotNull
    @Min(1)
    @Max(12)
    private Integer credits;

    @Size(min=3)
    private List<Long> degreeIds;
    @Size(min=3)
    private List<Integer> semesters;

    @NotNull
    private List<String> requirementIds;

    @NotNull
    private List<String> professors;

    @NotNull
    private List<SubjectClassForm> subjectClasses;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public List<Long> getDegreeIds() {
        return degreeIds;
    }

    public void setDegreeIds(List<Long> degreeIds) {
        this.degreeIds = degreeIds;
    }

    public List<Integer> getSemesters() {
        return semesters;
    }

    public void setSemesters(List<Integer> semesters) {
        this.semesters = semesters;
    }

    public List<String> getRequirementIds() {
        return requirementIds;
    }

    public void setRequirementIds(List<String> requirementIds) {
        this.requirementIds = requirementIds;
    }

    public List<String> getProfessors() {
        return professors;
    }

    public void setProfessors(List<String> professors) {
        this.professors = professors;
    }

    public List<SubjectClassForm> getSubjectClasses() {
        return subjectClasses;
    }

    public void setSubjectClasses(List<SubjectClassForm> subjectClasses) {
        this.subjectClasses = subjectClasses;
    }
}
