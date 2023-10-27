package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Degree;

import javax.ws.rs.core.UriInfo;

public class DegreeDto {

    private Long id;
    private String name;

    private Integer totalCredits;

    public static DegreeDto fromDegree(UriInfo uriInfo, Degree degree){
        final DegreeDto degreeDto = new DegreeDto();

        degreeDto.id = degree.getId();
        degreeDto.name = degree.getName();
        degreeDto.totalCredits = degree.getTotalCredits();

        return degreeDto;
    }

    public Long getId() {
        return id;
    }
    public void setId(){
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(){
        this.name = name;
    }
    public Integer getTotalCredits() {
        return totalCredits;
    }
    public void setTotalCredits(){
        this.totalCredits = totalCredits;
    }

}
