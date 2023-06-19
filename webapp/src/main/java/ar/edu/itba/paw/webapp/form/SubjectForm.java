package ar.edu.itba.paw.webapp.form;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;

public class SubjectForm {
    @NotNull
    @Pattern(regexp = "[0-9]2\\.[0-9]2")
    private String id;
    @Size(max=50)
    private String name;
    @Size(max=40)
    private String department;
    @NotNull
    @Max(12)
    @Min(1)
    private Integer credits;
    private String degreeIds;
    private String semesters;
    private String requirementIds;

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

    public void setDepartment(String deparment) {
        this.department = deparment;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public String getSemesters() {
        return semesters;
    }

    public void setSemesters(String semesters) {
        this.semesters = semesters;
    }

    public String getDegreeIds() {
        return degreeIds;
    }

    public void setDegreeIds(String degreeIds) {
        this.degreeIds = degreeIds;
    }

    public String getRequirementIds() {
        return requirementIds;
    }

    public void setRequirementIds(String requirementIds) {
        this.requirementIds = requirementIds;
    }
}
