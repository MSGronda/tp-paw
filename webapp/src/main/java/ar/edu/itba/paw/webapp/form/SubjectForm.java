package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.*;
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
    private String degreeIds;
    @Size(min=3)
    private String semesters;
    private String requirementIds;

    @Size(min=3)
    private String professors;

    @Size(min=3)
    private String classCodes;

    @Size(min=3)
    private String classProfessors;

    @Size(min=3)
    private String classDays;

    @Size(min=3)
    private String classStartTimes;

    @Size(min=3)
    private String classEndTimes;

    @Size(min=3)
    private String classBuildings;

    @Size(min=3)
    private String classRooms;
    @Size(min=3)
    private String classModes;

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

    public String getProfessors() {
        return professors;
    }

    public void setProfessors(String professors) {
        this.professors = professors;
    }

    public String getClassCodes() {
        return classCodes;
    }

    public void setClassCodes(String classCodes) {
        this.classCodes = classCodes;
    }

    public String getClassProfessors() {
        return classProfessors;
    }

    public void setClassProfessors(String classProfessors) {
        this.classProfessors = classProfessors;
    }

    public String getClassDays() {
        return classDays;
    }

    public void setClassDays(String classDays) {
        this.classDays = classDays;
    }

    public String getClassBuildings() {
        return classBuildings;
    }

    public void setClassBuildings(String classBuildings) {
        this.classBuildings = classBuildings;
    }

    public String getClassRooms() {
        return classRooms;
    }

    public void setClassRooms(String classRooms) {
        this.classRooms = classRooms;
    }

    public String getClassModes() {
        return classModes;
    }

    public void setClassModes(String classModes) {
        this.classModes = classModes;
    }

    public String getClassStartTimes() {
        return classStartTimes;
    }

    public void setClassStartTimes(String classStartTimes) {
        this.classStartTimes = classStartTimes;
    }

    public String getClassEndTimes() {
        return classEndTimes;
    }

    public void setClassEndTimes(String classEndTimes) {
        this.classEndTimes = classEndTimes;
    }
}
