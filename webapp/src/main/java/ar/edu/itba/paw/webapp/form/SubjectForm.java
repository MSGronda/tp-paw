package ar.edu.itba.paw.webapp.form;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;

public class SubjectForm {
    @NotNull
    @Pattern(regexp = "[0-9]{2}\\.[0-9]{2}")
    private String id;
    @Size(max=50)
    private String name;
    @Size(max=40)
    private String department;
    @NotNull
    private String credits;

    @NotNull
    private String degreeIds;
    @NotNull
    private String semesters;
    private String requirementIds;

    @NotNull
    private String professors;

    @NotNull
    private String classCodes;

    @NotNull
    private String classProfessors;

    @NotNull
    private String classDays;

    @NotNull
    private String classStartTimes;

    @NotNull
    private String classEndTimes;

    @NotNull
    private String classBuildings;

    @NotNull
    private String classRooms;
    @NotNull
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

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
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
