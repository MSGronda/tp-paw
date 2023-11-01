package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.exceptions.DegreeNotFoundException;
import ar.edu.itba.paw.services.AuthUserService;
import ar.edu.itba.paw.services.DegreeService;
import ar.edu.itba.paw.webapp.dto.DegreeDto;
import ar.edu.itba.paw.webapp.dto.SemesterDto;
import ar.edu.itba.paw.webapp.dto.SubjectDto;
import ar.edu.itba.paw.webapp.form.DegreeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
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

    //done
    @GET
    @Produces("application/vnd.degree-list.v1+json")
    public Response getDegrees(
            //params
    ) {
        List<Degree> degrees = degreeService.getAll();
        List<DegreeDto> degreeDtos = degrees.stream().map(degree -> DegreeDto.fromDegree(uriInfo, degree)).collect(Collectors.toList());
        if(degreeDtos.isEmpty()) {
            return Response.noContent().build();
        }
        Response.ResponseBuilder responseBuilder = Response.ok(new GenericEntity<List<DegreeDto>>(degreeDtos){});
        return responseBuilder.build();

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDegree(@Valid @ModelAttribute("degreeForm") final DegreeForm degreeForm) {
        Degree newDeg;
        try {
            degreeService.create(Degree.builder()
                    .name(degreeForm.getName())
                    .totalCredits(Integer.parseInt(degreeForm.getTotalCredits())
                    ));
        } catch (final IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.CREATED.getStatusCode()).build();
    }

    //done
    @GET
    @Path("/{id}")
    @Produces("application/vnd.degree.v1+json")
    public Response getDegree(@PathParam("id") final long id) {
        final Degree degree = degreeService.findById(id).orElseThrow(DegreeNotFoundException::new);
        Response.ResponseBuilder response = Response.ok(DegreeDto.fromDegree(uriInfo, degree));
        return response.build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateDegree(
            @Valid @ModelAttribute("degreeForm") final DegreeForm degreeForm,
            @PathParam("id") final long id) {
        final Degree degree = degreeService.findById(id).orElseThrow(DegreeNotFoundException::new);
        //TODO degree.update(degreeForm.getName(), degreeFOrm.subjects());
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteDegree(@PathParam("id") final long id) {
        //TODO: delete method in DegreeService
        return Response.noContent().build();
    }

    //done
    @GET
    @Path("/{id}/semesters")
    @Produces("application/vnd.degree.semesters.list.v1+json")
    public Response getDegreeSemesters(@PathParam("id") final long id) {
        final Degree degree = degreeService.findById(id).orElseThrow(DegreeNotFoundException::new);
        final List<SemesterDto> semesters = degree.getSemesters().stream().map(semester -> SemesterDto.fromSemester(uriInfo, degree, semester.getNumber())).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<SemesterDto>>(semesters){}).build();
    }

    @POST
    @Path("/{id}/semesters")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSemester(@PathParam("id") final long id) {
        return Response.ok().build();
    }


    //done
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

    @PUT
    @Path("/{degreeId}/semesters/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editDegreeSemester(
            @PathParam("degreeId") final long degreeId,
            @PathParam("id") final long id
    ) {
        return Response.ok().build();
    }

    @DELETE
    @Path("/{degreeId}/semesters/{id}")
    public Response deleteDegreeSemester(
            @PathParam("degreeId") final long degreeId,
            @PathParam("id") final long id
    ) {
        return Response.ok().build();
    }

}
