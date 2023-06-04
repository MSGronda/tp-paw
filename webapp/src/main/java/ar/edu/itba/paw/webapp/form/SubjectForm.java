package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class SubjectForm {
    @NotNull
    private Long id;
    @Size(max=30)
    private String name;
    @Size(max=40)
    private String department;
    @NotNull
    private Integer credits;
    @NotNull
    private Integer semester;
    private List<Integer> professorIds;
    private List<Integer>degreeIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public List<Integer> getProfessorIds() {
        return professorIds;
    }

    public void setProfessorIds(List<Integer> professorIds) {
        this.professorIds = professorIds;
    }

    public List<Integer> getDegreeIds() {
        return degreeIds;
    }

    public void setDegreeIds(List<Integer> degreeIds) {
        this.degreeIds = degreeIds;
    }
}
