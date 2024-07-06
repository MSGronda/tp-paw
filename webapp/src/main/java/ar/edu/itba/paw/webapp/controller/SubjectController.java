package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.*;
import ar.edu.itba.paw.models.utils.SubjectSearchParams;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.controller.utils.PaginationLinkBuilder;
import ar.edu.itba.paw.webapp.dto.SubjectDto;
import ar.edu.itba.paw.webapp.dto.SubjectsFiltersDto;
import ar.edu.itba.paw.webapp.form.SubjectForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Path("subjects")
@Component
public class SubjectController {

    @Autowired
    private SubjectService subjectService;
    @Autowired
    private AuthUserService authUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProfessorService professorService;
    @Autowired
    private DegreeService degreeService;
    @Context
    private UriInfo uriInfo;
    
    @GET
    @Produces("application/vnd.subject-list.v1+json")
    public Response getSubjects(
            @QueryParam("degree") final Long degree,
            @QueryParam("semester") final Long semester,

            @QueryParam("available") final Long available,
            @QueryParam("unLockable") final Long unLockable,
            @QueryParam("done") final Long done,
            @QueryParam("future") final Long future,

            @QueryParam("plan") final Long plan,
            @QueryParam("planFinishedDate") final Long planFinishedDate,

            @QueryParam("q") final String query,
            @QueryParam("credits") final Integer credits,
            @QueryParam("department") final String department,
            @QueryParam("difficulty") final Integer difficulty,
            @QueryParam("timeDemand") final Integer timeDemand,

            @QueryParam("userReviews") final Long userReviews,

            @QueryParam("page") @DefaultValue("1") final int page,
            @QueryParam("orderBy") @DefaultValue("name") final String orderBy,
            @QueryParam("dir") @DefaultValue("asc") final String dir
        ){

        final SubjectSearchParams params = new SubjectSearchParams(
                degree,
                semester,
                available,
                unLockable,
                done,
                future,
                plan,
                planFinishedDate,
                query,
                credits,
                department,
                difficulty,
                timeDemand,
                userReviews
        );

        final List<Subject> subs = subjectService.superSearch(params, page, orderBy, dir);

        if (subs.isEmpty()){
            return Response.noContent().build();
        }

        Response.ResponseBuilder responseBuilder = Response.ok(new SubjectsFiltersDto(
                subs.stream().map(subject -> SubjectDto.fromSubjectWithSemesters(uriInfo, subject, degree, semester)).collect(Collectors.toList()),
                subjectService.superSearchRelevantFilters(params)
        ));

        PaginationLinkBuilder.getResponsePaginationLinks(responseBuilder, uriInfo, page, subjectService.superSearchTotalPages(params));
        return responseBuilder.build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSubject(@Valid @ModelAttribute("subjectForm") final SubjectForm subjectForm) {
        final Subject newSub = subjectService.create(
            Subject.builder()
                    .id(subjectForm.getId())
                    .name(subjectForm.getName())
                    .department(subjectForm.getDepartment())
                    .credits(subjectForm.getCredits()),
            subjectForm.getDegreeIds(),
            subjectForm.getSemesters(),
            subjectForm.getRequirementIds(),
            subjectForm.getProfessors()
        );

        subjectService.addClasses(
                newSub,
                subjectForm.getCollectedCodes(),
                subjectForm.getCollectedProfessors(),
                subjectForm.getCollectedDays(),
                subjectForm.getCollectedStartTimes(),
                subjectForm.getCollectedEndTimes(),
                subjectForm.getCollectedLocations(),
                subjectForm.getCollectedBuildings(),
                subjectForm.getCollectedModes()
        );

        return Response.status(Response.Status.CREATED.getStatusCode()).build();
    }

    @GET
    @Path("/{id}")
    public Response getSubjectById(@PathParam("id") final String subjectId){
        final Subject subject = subjectService.findById(subjectId).orElseThrow(SubjectNotFoundException::new);
        return Response.ok(SubjectDto.fromSubject(uriInfo,subject)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteSubject(@PathParam("id") final String subjectId){
        final User user = authUserService.getCurrentUser();
        subjectService.delete(user, subjectId);
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}")
    public Response editSubject(
            @PathParam("id") final String subjectId,
            @Valid @ModelAttribute("subjectForm") final SubjectForm subjectForm
    ){
        final Subject subject = subjectService.findById(subjectId).orElseThrow(SubjectNotFoundException::new);

        subjectService.editSubject(
                subject,
                subjectForm.getName(),
                subjectForm.getDepartment(),
                subjectForm.getCredits(),
                subjectForm.getRequirementIds()
        );

        degreeService.replaceSubjectDegrees(subject, subjectForm.getDegreeIds(), subjectForm.getSemesters());

        professorService.replaceSubjectProfessors(subject, subjectForm.getProfessors());

        subjectService.setClasses(
                subject,
                subjectForm.getCollectedCodes(),
                subjectForm.getCollectedProfessors(),
                subjectForm.getCollectedDays(),
                subjectForm.getCollectedStartTimes(),
                subjectForm.getCollectedEndTimes(),
                subjectForm.getCollectedLocations(),
                subjectForm.getCollectedBuildings(),
                subjectForm.getCollectedModes()
        );

        return Response.ok().build();
    }
}
