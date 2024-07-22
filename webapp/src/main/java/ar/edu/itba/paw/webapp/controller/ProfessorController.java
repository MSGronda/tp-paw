package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.exceptions.ProfessorNotFoundException;
import ar.edu.itba.paw.services.ProfessorService;
import ar.edu.itba.paw.webapp.controller.utils.PaginationLinkBuilder;
import ar.edu.itba.paw.webapp.controller.utils.UriUtils;
import ar.edu.itba.paw.webapp.dto.ProfessorDto;
import ar.edu.itba.paw.webapp.dto.ReviewVoteDto;
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

@Path(UriUtils.PROFESSOR_BASE)
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
    public Response getProfessors(
            @QueryParam("subjectId") final String subjectId,
            @QueryParam("classId") final String classId,
            @QueryParam("q") final String q,
            @QueryParam("page") @DefaultValue("1") final int page
    ) {
        final List<Professor> professors = professorService.searchProfessors(subjectId, classId, q, page);

        if(professors.isEmpty()){
            return Response.noContent().build();
        }

        final List<ProfessorDto> professorsDtos = professors.stream().map(professor -> ProfessorDto.fromProfessor(uriInfo, professor)).collect(Collectors.toList());

        int lastPage = professorService.getTotalPagesForSearch(subjectId, classId, q);
        final Response.ResponseBuilder responseBuilder = Response.ok(new GenericEntity<List<ProfessorDto>>(professorsDtos){});
        PaginationLinkBuilder.getResponsePaginationLinks(responseBuilder, uriInfo, page, lastPage);

        return responseBuilder.build();
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

        return Response.created(UriUtils.createdProfessorUri(uriInfo, professor)).build();
    }

}
