package ar.edu.itba.paw.webapp.dto;
import ar.edu.itba.paw.models.Degree;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

public class DegreeDto {

    private Long id;
    private String name;
    private Integer totalCredits;
    private List<URI> subjects;

    public static DegreeDto fromDegree(UriInfo uriInfo, Degree degree){
        final DegreeDto degreeDto = new DegreeDto();

        degreeDto.id = degree.getId();
        degreeDto.name = degree.getName();
        degreeDto.totalCredits = degree.getTotalCredits();
        degreeDto.subjects = getSubjectURIs(uriInfo, degree);

        return degreeDto;
    }

    //getters
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Integer getTotalCredits() {
        return totalCredits;
    }

    public List<URI> getSubjects() {
        return subjects;
    }

    //setters
    public void setId(Long id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setTotalCredits(Integer totalCredits) {
        this.totalCredits = totalCredits;
    }
    public void setSubjects(List<URI> subjects) {
        this.subjects = subjects;
    }

    private static List<URI> getSubjectURIs(UriInfo uriInfo, Degree degree){
        return degree.getDegreeSubjects()
                .stream()
                .map(subject -> uriInfo.getBaseUriBuilder()
                        .path("subjects")
                        .queryParam("degree", degree.getId())
                        .queryParam("semester",String.valueOf(subject.getSemester()))
                        .build())
                .collect(java.util.stream.Collectors.toList());
    }
}
