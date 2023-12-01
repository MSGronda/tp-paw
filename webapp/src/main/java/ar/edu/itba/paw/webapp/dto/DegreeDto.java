package ar.edu.itba.paw.webapp.dto;
import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.DegreeSemester;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

public class DegreeDto {

    private Long id;
    private String name;
    private Integer totalCredits;
    private List<URI> semesterSubjects;

    public static DegreeDto fromDegree(UriInfo uriInfo, Degree degree){
        final DegreeDto degreeDto = new DegreeDto();

        degreeDto.id = degree.getId();
        degreeDto.name = degree.getName();
        degreeDto.totalCredits = degree.getTotalCredits();
        degreeDto.semesterSubjects = getSubjectURIs(uriInfo, degree);

        return degreeDto;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public Integer getTotalCredits() {
        return totalCredits;
    }
    public List<URI> getSemesterSubjects() {
        return semesterSubjects;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }
    public void setTotalCredits(Integer totalCredits) {
        this.totalCredits = totalCredits;
    }
    public void setSemesterSubjects(List<URI> semesterSubjects) {
        this.semesterSubjects = semesterSubjects;
    }

    private static List<URI> getSubjectURIs(final UriInfo uriInfo, final Degree degree){
        final List<URI> uris = degree.getSemesters()
                .stream()
                .map(semester -> uriInfo.getBaseUriBuilder()
                        .path("subjects")
                        .queryParam("degree", degree.getId())
                        .queryParam("semester", String.valueOf(semester.getNumber()))
                        .build())
                .collect(java.util.stream.Collectors.toList());

        uris.add(uriInfo.getBaseUriBuilder()
                .path("subjects")
                .queryParam("degree", degree.getId())
                .queryParam("semester", String.valueOf(Degree.getElectiveId()))
                .build()
        );
        return uris;
    }
}
