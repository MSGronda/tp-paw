package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.DegreeSemester;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.DegreeNotFoundException;
import ar.edu.itba.paw.models.exceptions.SemesterNotFoundException;
import ar.edu.itba.paw.models.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.models.utils.DegreeSearchParams;
import ar.edu.itba.paw.services.AuthUserService;
import ar.edu.itba.paw.services.DegreeService;
import ar.edu.itba.paw.services.SubjectService;
import ar.edu.itba.paw.webapp.dto.DegreeDto;
import ar.edu.itba.paw.webapp.dto.SemesterDto;
import ar.edu.itba.paw.webapp.form.DegreeForm;
import ar.edu.itba.paw.webapp.form.DegreeSemesterForm;
import ar.edu.itba.paw.webapp.form.EditDegreeSemesterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Path("degrees")
@Component
public class DegreeController {

    private final DegreeService degreeService;
    private final AuthUserService authUserService;
    private final SubjectService subjectService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public DegreeController(final DegreeService degreeService, final AuthUserService authUserService, final SubjectService subjectService){
        this.degreeService = degreeService;
        this.authUserService = authUserService;
        this.subjectService = subjectService;
    }

    @GET
    @Produces("application/vnd.degree-list.v1+json")
    public Response getDegrees(
            @QueryParam("subjectId") final String subjectId
    ) {
        final DegreeSearchParams params = new DegreeSearchParams(subjectId);
        final List<Degree> degrees = degreeService.searchDegrees(params);

        final List<DegreeDto> degreeDtos = degrees.stream().map(degree -> DegreeDto.fromDegree(uriInfo, degree)).collect(Collectors.toList());
        if(degreeDtos.isEmpty()) {
            return Response.noContent().build();
        }

        return Response.ok(new GenericEntity<List<DegreeDto>>(degreeDtos){}).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDegree(@Valid @ModelAttribute("degreeForm") final DegreeForm degreeForm) {
        final Degree degree = degreeService.create(Degree.builder()
                .name(degreeForm.getName())
                .totalCredits(degreeForm.getTotalCredits())
        );

        // TODO: CHECK si es correcto hacerlo aca
        final URI uri = uriInfo.getBaseUriBuilder().path("degrees").path(String.valueOf(degree.getId())).build();

        return Response.created(uri).entity(DegreeDto.fromDegree(uriInfo, degree)) .build();
    }


    @GET
    @Path("/{id}")
    @Produces("application/vnd.degree.v1+json")
    public Response getDegreeById(@PathParam("id") final long id) {
        final Degree degree = degreeService.findById(id).orElseThrow(DegreeNotFoundException::new);

        return Response.ok(DegreeDto.fromDegree(uriInfo, degree)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteDegree(@PathParam("id") final long id) {
        final Degree degree = degreeService.findById(id).orElseThrow(DegreeNotFoundException::new);

        degreeService.delete(degree);
        return Response.status(Response.Status.NO_CONTENT.getStatusCode()).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editDegree(
            @PathParam("id") final long id,
            @Valid final DegreeForm degreeForm
    ){
        final Degree degree = degreeService.findById(id).orElseThrow(DegreeNotFoundException::new);

        degreeService.editName(degree, degreeForm.getName());
        degreeService.editTotalCredits(degree, degreeForm.getTotalCredits());

        return Response.ok().build();
    }

    @GET
    @Path("/{id}/semesters")
    @Produces("application/vnd.degree.semesters.list.v1+json")
    public Response getDegreeSemesters(@PathParam("id") final long id, @QueryParam("subjectId") final String subjectId) {
        final Degree degree = degreeService.findById(id).orElseThrow(DegreeNotFoundException::new);
        final List<DegreeSemester> semesters = degreeService.getDegreeSemesters(id, subjectId);
        final List<SemesterDto> semestersDtos = semesters.stream().map(semester -> SemesterDto.fromSemester(uriInfo, degree, semester.getNumber())).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<SemesterDto>>(semestersDtos){}).build();
    }

    @GET
    @Path("/{degreeId}/semesters/{id}")
    @Produces("application/vnd.degree.semester.v1+json")
    public Response getDegreeSemester(
            @PathParam("degreeId") final long degreeId,
            @PathParam("id") final long id
    ) {
        final Degree degree = degreeService.findById(degreeId).orElseThrow(DegreeNotFoundException::new);

        return Response.ok(SemesterDto.fromSemester(uriInfo, degree, (int)id)).build();
    }

    @POST
    @Path("/{id}/semesters")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSemester(
            @PathParam("id") final long id,
            @Valid final DegreeSemesterForm degreeSemesterForm
    ) {
        final Degree degree = degreeService.findById(id).orElseThrow(DegreeNotFoundException::new);
        degreeService.addSemestersToDegree(degree, degreeSemesterForm.getSemesterMap());

        return Response.status(Response.Status.CREATED.getStatusCode()).build();
    }


    @PATCH
    @Path("/{degreeId}/semesters")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editDegreeSemester(
            @PathParam("degreeId") final long degreeId,
            @Valid final EditDegreeSemesterForm editDegreeSemesterForm
    ) {
        final Degree degree = degreeService.findById(degreeId).orElseThrow(DegreeNotFoundException::new);

        editDegreeSemesterForm.getOperationsForEdit().forEach(op -> degreeService.editDegreeSemester(degree,op));

        return Response.ok().build();
    }

    @DELETE
    @Path("/{degreeId}/semesters/{id}")
    public Response deleteDegreeSemester(
            @PathParam("degreeId") final long degreeId,
            @PathParam("id") final int id
    ) {
        final Degree degree = degreeService.findById(degreeId).orElseThrow(DegreeNotFoundException::new);
        degreeService.deleteSemesterFromDegree(degree, id);

        return Response.status(Response.Status.NO_CONTENT.getStatusCode()).build();
    }

    @GET
    @Path("/{subjectId}/year")
    public Response getSubjectYearForParentDegree(
            @PathParam("subjectId") final String subjectId
    ) {
        final Subject subject = subjectService.findById(String.valueOf(subjectId)).orElseThrow(SubjectNotFoundException::new);
        final User user = authUserService.getCurrentUser();
        final Degree degree = degreeService.findParentDegree(subject, user).orElseThrow(DegreeNotFoundException::new);
        final int semesterId = degreeService.findSubjectYearForParentDegree(subject, user).orElseThrow(SemesterNotFoundException::new);

        return Response.ok(SemesterDto.fromSemester(uriInfo, degree, semesterId)).build();
    }

    @GET
    @Path("/{subjectId}/degree")
    public Response getDegreeForSubject(
            @PathParam("subjectId") final String subjectId
    ) {
        final Subject subject = subjectService.findById(String.valueOf(subjectId)).orElseThrow(SubjectNotFoundException::new);
        final User user = authUserService.getCurrentUser();
        final Degree degree = degreeService.findParentDegree(subject, user).orElseThrow(DegreeNotFoundException::new);

        return Response.ok(DegreeDto.fromDegree(uriInfo, degree)).build();
    }

}
