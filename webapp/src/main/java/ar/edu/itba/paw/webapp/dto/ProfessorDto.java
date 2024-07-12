package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Professor;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

public class ProfessorDto {
    private String name;
    private List<URI> subjects;

    public static ProfessorDto fromProfessor(UriInfo uriInfo, Professor professor){
        final ProfessorDto professorDto = new ProfessorDto();

        professorDto.name = professor.getName();
        professorDto.subjects = getSubjectURIs(uriInfo, professor);

        return professorDto;
    }


    private static List<URI> getSubjectURIs(final UriInfo uriInfo, final Professor professor){
        return professor.getSubjects().stream()
                .map(subject -> uriInfo.getBaseUriBuilder()
                        .path("subjects/")
                        .path(subject.getId())
                        .build())
                .collect(Collectors.toList());

    }

    public String getName() {
        return name;
    }

    public List<URI> getSubjects() {
        return subjects;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubjects(List<URI> subjects) {
        this.subjects = subjects;
    }
}
