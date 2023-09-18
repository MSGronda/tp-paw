package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Subject;

import javax.ws.rs.core.UriInfo;

public class SubjectDto {
    private String id;
    private String name;


    public static SubjectDto fromSubject(UriInfo uriInfo, Subject subject){
        SubjectDto subjectDto =  new SubjectDto();

        // TODO: completar

        subjectDto.id = subject.getId();
        subjectDto.name = subject.getName();

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
}
