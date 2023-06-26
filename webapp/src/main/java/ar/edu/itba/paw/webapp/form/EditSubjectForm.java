package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EditSubjectForm {
    @NotNull
    @Min(1)
    @Max(12)
    private Integer credits;

    @NotBlank
    private String degreeIds;

    @NotBlank
    private String semesters;

    private String requirementIds;

    @NotBlank
    private String professors;


    private String classIds;

    private String classCodes;

    private String classProfessors;

    private String classDays;

    private String classStartTimes;

    private String classEndTimes;

    private String classBuildings;

    private String classRooms;

    private String classModes;

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

    public String getClassIds() {
        return classIds;
    }

    public void setClassIds(String classIds) {
        this.classIds = classIds;
    }
}
