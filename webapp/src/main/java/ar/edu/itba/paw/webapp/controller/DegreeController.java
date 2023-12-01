package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.exceptions.DegreeNotFoundException;
import ar.edu.itba.paw.services.AuthUserService;
import ar.edu.itba.paw.services.DegreeService;
import ar.edu.itba.paw.webapp.dto.DegreeDto;
import ar.edu.itba.paw.webapp.dto.SemesterDto;
import ar.edu.itba.paw.webapp.form.DegreeForm;
import ar.edu.itba.paw.webapp.form.DegreeSemesterForm;
import org.eclipse.persistence.jaxb.json.JsonSchemaOutputResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

@Path("degrees")
@Component
public class DegreeController {
    @Autowired
    private DegreeService degreeService;
    @Autowired
    private AuthUserService authUserService;
    @Context
    private UriInfo uriInfo;

    /*
    @Autowired
    public DegreeController(
            final DegreeService degreeService,
            final AuthUserService authUserService
    ) {
        this.degreeService = degreeService;
        this.authUserService = authUserService;
    }

    @RequestMapping("/degrees")
    public ModelAndView degrees() {
        final ModelAndView mav = new ModelAndView("degree/degrees");
        mav.addObject("degrees", degreeService.getAll());
        return mav;
    }

    @RequestMapping("/degree")
    public ModelAndView degree() {
        return new ModelAndView("redirect:/degree/" + authUserService.getCurrentUser().getDegree().getId());
    }

    @RequestMapping("/degree/{id:\\d+}")
    public ModelAndView degree(@PathVariable final long id) {
        final Degree degree = degreeService.findById(id).orElseThrow(DegreeNotFoundException::new);

        final ModelAndView mav = new ModelAndView("degree/index");
        mav.addObject("degree", degree);
        mav.addObject("subjectProgress", authUserService.getCurrentUser().getAllSubjectProgress());
        mav.addObject("relevantFilters", degreeService.getRelevantFiltersForDegree(degree));
        return mav;
    }
    */

    @GET
    @Produces("application/vnd.degree-list.v1+json")
    public Response getDegrees(
        // TODO: params (?)
    ) {
        System.out.println("getting");

        final List<Degree> degrees = degreeService.getAll();
        final List<DegreeDto> degreeDtos = degrees.stream().map(degree -> DegreeDto.fromDegree(uriInfo, degree)).collect(Collectors.toList());
        if(degreeDtos.isEmpty()) {
            return Response.noContent().build();
        }

        return Response.ok(new GenericEntity<List<DegreeDto>>(degreeDtos){}).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDegree(@Valid @ModelAttribute("degreeForm") final DegreeForm degreeForm) {
        degreeService.create(Degree.builder()
                .name(degreeForm.getName())
                .totalCredits(degreeForm.getTotalCredits())
        );
        return Response.status(Response.Status.CREATED.getStatusCode()).build();
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

    @GET
    @Path("/{id}/semesters")
    @Produces("application/vnd.degree.semesters.list.v1+json")
    public Response getDegreeSemesters(@PathParam("id") final long id) {
        final Degree degree = degreeService.findById(id).orElseThrow(DegreeNotFoundException::new);
        final List<SemesterDto> semesters = degree.getSemesters().stream().map(semester -> SemesterDto.fromSemester(uriInfo, degree, semester.getNumber())).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<SemesterDto>>(semesters){}).build();
    }

    @GET
    @Path("/{degreeId}/semesters/{id}")
    @Produces("application/vnd.degree.semester.v1+json")
    public Response getDegreeSemester(
            @PathParam("degreeId") final long degreeId,
            @PathParam("id") final long id
    ) {
        final Degree degree = degreeService.findById(degreeId).orElseThrow(DegreeNotFoundException::new);

        return Response.ok( SemesterDto.fromSemester(uriInfo, degree, (int)id)).build();
    }


    @POST
    @Path("/{id}/semesters")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSemester(
            @PathParam("id") final long id,
            @Valid @ModelAttribute("degreeSemesterForm") final DegreeSemesterForm degreeSemesterForm
    ) {

        // TODO: fix el form de mierda que no funciona

        final Degree degree = degreeService.findById(id).orElseThrow(DegreeNotFoundException::new);
        degreeService.addSemestersToDegree(degree, degreeSemesterForm.getSemesters());

        return Response.status(Response.Status.CREATED.getStatusCode()).build();
    }

    @PUT
    @Path("/{id}/semesters")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response replaceDegreesSemesters(
            @PathParam("id") final long id,
            @Valid @ModelAttribute("degreeSemesterForm") final DegreeSemesterForm degreeSemesterForm
    ) {
        final Degree degree = degreeService.findById(id).orElseThrow(DegreeNotFoundException::new);
        degreeService.replaceSemestersInDegree(degree, degreeSemesterForm.getSemesters());

        return Response.status(Response.Status.ACCEPTED.getStatusCode()).build();  // TODO CHECK accepted
    }

    @PUT
    @Path("/{degreeId}/semesters/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editDegreeSemester(
            @PathParam("degreeId") final long degreeId,
            @PathParam("id") final long id
    ) {
        // TODO
        return Response.ok().build();
    }

    @DELETE
    @Path("/{degreeId}/semesters/{id}")
    public Response deleteDegreeSemester(
            @PathParam("degreeId") final long degreeId,
            @PathParam("id") final long id
    ) {
        // TODO
        return Response.ok().build();
    }

}
