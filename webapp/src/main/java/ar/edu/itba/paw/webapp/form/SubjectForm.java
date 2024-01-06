package ar.edu.itba.paw.webapp.form;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class SubjectForm {
    @NotNull
    @Pattern(regexp = "[0-9]{2}\\.[0-9]{2}")
    private String id;

    @Size(min=1, max=50)
    private String name;

    @Size(min=1, max=40)
    private String department;

    @NotNull
    private Integer credits;

    @NotNull
    private List<Long> degreeIds;

    @NotNull
    private List<Integer> semesters;

    @NotNull
    private List<String> requirementIds;

    @NotNull
    private List<String> professors;

    private boolean collected = false;
    private List<String> collectedCodes;
    private List<List<String>> collectedProfessors;
    private List<List<Integer>> collectedDays;
    private List<List<LocalTime>> collectedStartTimes;
    private List<List<LocalTime>> collectedEndTimes;
    private List<List<String>> collectedLocations;
    private List<List<String>> collectedBuildings;
    private List<List<String>> collectedModes;

    @NotNull
    @Valid
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

    private void collectClasses(){
        if(collected){
            return;
        }

        collected = true;

        collectedCodes = new ArrayList<>();
        collectedProfessors = new ArrayList<>();
        collectedDays = new ArrayList<>();
        collectedStartTimes = new ArrayList<>();
        collectedEndTimes = new ArrayList<>();
        collectedLocations = new ArrayList<>();
        collectedBuildings = new ArrayList<>();
        collectedModes = new ArrayList<>();

        subjectClasses.forEach(classForm -> {
            collectedCodes.add(classForm.getCode());
            collectedProfessors.add(classForm.getProfessors());
            collectedDays.add(classForm.getClassTimes().stream().map(SubjectClassTimeForm::getDay).collect(Collectors.toList()));
            collectedStartTimes.add(classForm.getClassTimes().stream().map(SubjectClassTimeForm::getStartTime).collect(Collectors.toList()));
            collectedEndTimes.add(classForm.getClassTimes().stream().map(SubjectClassTimeForm::getEndTime).collect(Collectors.toList()));
            collectedLocations.add(classForm.getClassTimes().stream().map(SubjectClassTimeForm::getLocation).collect(Collectors.toList()));
            collectedBuildings.add(classForm.getClassTimes().stream().map(SubjectClassTimeForm::getBuilding).collect(Collectors.toList()));
            collectedModes.add(classForm.getClassTimes().stream().map(SubjectClassTimeForm::getMode).collect(Collectors.toList()));
        });
    }

    public List<String> getCollectedCodes() {
        if(!collected){
            collectClasses();
        }
        return collectedCodes;
    }

    public List<List<String>> getCollectedProfessors() {
        if(!collected){
            collectClasses();
        }
        return collectedProfessors;
    }

    public List<List<Integer>> getCollectedDays() {
        if(!collected){
            collectClasses();
        }
        return collectedDays;
    }

    public List<List<LocalTime>> getCollectedStartTimes() {
        if(!collected){
            collectClasses();
        }
        return collectedStartTimes;
    }

    public List<List<LocalTime>> getCollectedEndTimes() {
        if(!collected){
            collectClasses();
        }
        return collectedEndTimes;
    }

    public List<List<String>> getCollectedLocations() {
        if(!collected){
            collectClasses();
        }
        return collectedLocations;
    }

    public List<List<String>> getCollectedBuildings() {
        if(!collected){
            collectClasses();
        }
        return collectedBuildings;
    }

    public List<List<String>> getCollectedModes() {
        if(!collected){
            collectClasses();
        }
        return collectedModes;
    }
}
