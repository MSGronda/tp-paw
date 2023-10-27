package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Subject;

import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

public class SubjectDto {
    private String id;
    private String name;
    private String department;
    private Integer credits;

    private List<ClassDto> classes;

    public static SubjectDto fromSubject(UriInfo uriInfo, Subject subject){
        final SubjectDto subjectDto =  new SubjectDto();

        subjectDto.id = subject.getId();
        subjectDto.name = subject.getName();
        subjectDto.department = subject.getDepartment();
        subjectDto.credits = subject.getCredits();
        subjectDto.classes = subject.getClasses().stream().map(subjectClass -> ClassDto.fromClass(uriInfo, subjectClass)).collect(Collectors.toList());

        return subjectDto;
    }

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

    public List<ClassDto> getClasses() {
        return classes;
    }

    public void setClasses(List<ClassDto> classes) {
        this.classes = classes;
    }
}
