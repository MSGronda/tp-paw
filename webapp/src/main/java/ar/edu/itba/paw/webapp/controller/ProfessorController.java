package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.exceptions.ProfessorNotFoundException;
import ar.edu.itba.paw.services.ProfessorService;
import ar.edu.itba.paw.webapp.dto.ProfessorDto;
import ar.edu.itba.paw.webapp.dto.SubjectDto;
import ar.edu.itba.paw.webapp.form.ProfessorForm;
import ar.edu.itba.paw.webapp.form.SubjectForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

@Path("professors")
@Component
public class ProfessorController {
    private final ProfessorService professorService;

    @Context
    private UriInfo uriInfo;
    @Autowired
    public ProfessorController(final ProfessorService professorService){
        this.professorService = professorService;
    }

    @GET
    @Produces("application/vnd.professor-list.v1+json")
    public Response getProfessors() {
        final List<Professor> professors = professorService.getAll();
        final List<ProfessorDto> professorsDtos = professors.stream().map(professor -> ProfessorDto.fromProfessor(uriInfo, professor)).collect(Collectors.toList());

        if (professorsDtos.isEmpty()){
            return Response.noContent().build();
        }

        return Response.ok(new GenericEntity<List<ProfessorDto>>(professorsDtos){}).build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/vnd.professor-list.v1+json")
    public Response getProfessor(@PathParam("id") final Integer professorId) {
        final Professor professor = professorService.findById(professorId).orElseThrow(ProfessorNotFoundException::new);
        return Response.ok(ProfessorDto.fromProfessor(uriInfo, professor)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProfessor(@Valid @ModelAttribute("professorForm") final ProfessorForm professorForm){
        final Professor professor = professorService.createProfessor(professorForm.getName());

        return Response.created(
                uriInfo.getBaseUriBuilder().path("professors").path(String.valueOf(professor.getId())).build()
        ).build();
    }

}
