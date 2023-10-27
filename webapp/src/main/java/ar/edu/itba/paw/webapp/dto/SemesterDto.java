package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Degree;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class SemesterDto {

    private URI subjects;
    private Integer semester;

    public static SemesterDto fromSemester(final UriInfo uriInfo,
                                           final Degree degree,
                                           final Integer semesterId){
        SemesterDto semesterDto = new SemesterDto();
        semesterDto.semester = semesterId;
        semesterDto.subjects = uriInfo.getBaseUriBuilder().path("subjects").queryParam(String.valueOf(degree.getId())).queryParam("semester", String.valueOf(semesterId)).build();

        return semesterDto;
    }

    //getter
    public URI getSubjects() {
        return subjects;
    }
    public Integer getSemester() {
        return semester;
    }
    //setter
    public void setSubjects(URI subjects) {
        this.subjects = subjects;
    }
    public void setSemester(Integer semester) {
        this.semester = semester;
    }

}
