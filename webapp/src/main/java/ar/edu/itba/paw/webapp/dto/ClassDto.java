package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.SubjectClass;
import ar.edu.itba.paw.models.SubjectClassTime;

import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

public class ClassDto {
    private String idSubject;
    private String idClass;
    private List<String> professors;
    private List<LocationDto> locations;

    public static ClassDto fromClass(UriInfo uriInfo, SubjectClass subjectClass){
        final ClassDto classDto = new ClassDto();

        classDto.idSubject = subjectClass.getSubject().getId();
        classDto.idClass = subjectClass.getClassId();
        classDto.professors = subjectClass.getProfessors().stream().map(Professor::getName).collect(Collectors.toList());
        classDto.locations = subjectClass.getClassTimes().stream().map(subjectClassTime -> LocationDto.fromLocation(uriInfo, subjectClassTime)).collect(Collectors.toList());

        return classDto;
    }

    public String getIdSubject() {
        return idSubject;
    }

    public String getIdClass() {
        return idClass;
    }

    public List<String> getProfessors() {
        return professors;
    }

    public List<LocationDto> getLocations() {
        return locations;
    }

    public void setIdSubject(String idSubject) {
        this.idSubject = idSubject;
    }

    public void setIdClass(String idClass) {
        this.idClass = idClass;
    }

    public void setProfessors(List<String> professors) {
        this.professors = professors;
    }

    public void setLocations(List<LocationDto> locations) {
        this.locations = locations;
    }
}
