package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.services.ProfessorService;
import ar.edu.itba.paw.webapp.dto.ProfessorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

@Path("professors")
@Controller
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

}
