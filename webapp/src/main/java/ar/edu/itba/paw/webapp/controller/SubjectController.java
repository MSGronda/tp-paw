package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.*;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.controller.utils.PaginationLinkBuilder;
import ar.edu.itba.paw.webapp.dto.SubjectDto;
import ar.edu.itba.paw.webapp.dto.SubjectsFiltersDto;
import ar.edu.itba.paw.webapp.form.SubjectClassTimeForm;
import ar.edu.itba.paw.webapp.form.SubjectForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.time.LocalTime;
import java.util.ArrayList;
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

            @QueryParam("q") final String query,
            @QueryParam("credits") final Integer credits,
            @QueryParam("department") final String department,
            @QueryParam("difficulty") final Integer difficulty,
            @QueryParam("timeDemand") final Integer timeDemand,

            @QueryParam("page") @DefaultValue("1") final int page,
            @QueryParam("orderBy") @DefaultValue("name") final String orderBy,
            @QueryParam("dir") @DefaultValue("asc") final String dir
        ){

        final User user = userService.getRelevantUser(available, unLockable, done, future, plan).orElseThrow(UserNotFoundException::new);

        final List<Subject> subs = subjectService.get(
            user,
            degree,
            semester,
            available,
            unLockable,
            done,
            future,
            plan,
            query,
            credits,
            department,
            difficulty,
            timeDemand,
            page,
            orderBy,
            dir
        );


        final List<SubjectDto> subjectsDtos = subs.stream().map(subject -> SubjectDto.fromSubject(uriInfo, subject)).collect(Collectors.toList());

        if(subjectsDtos.isEmpty())
            return Response.noContent().build();

        if (query != null ) {
            int lastPage = subjectService.getTotalPagesForSearch(
                    user,
                    query,
                    credits,
                    department,
                    difficulty,
                    timeDemand,
                    orderBy
            );
            Map<String, List<String>> filters = subjectService.getRelevantFiltersForSearch(user, query, credits, department, difficulty, timeDemand, orderBy);
            Response.ResponseBuilder responseBuilder = Response.ok(new SubjectsFiltersDto(subjectsDtos, filters));
            PaginationLinkBuilder.getResponsePaginationLinks(responseBuilder, uriInfo, page, lastPage);
            return responseBuilder.build();
        }
        Response.ResponseBuilder responseBuilder = Response.ok(new GenericEntity<List<SubjectDto>>(subjectsDtos){});
        return responseBuilder.build();
    }

    @GET
    @Path("/userReviews")
    @Produces("application/vnd.user.v1+json")
    public Response getSubjectsThatUserReviewed(
            @QueryParam("userId") final Long userId,
            @QueryParam("page") @DefaultValue("0") final Integer page
    ){
        final List<Subject> subjects = subjectService.getSubjectsThatUserReviewed(userId, page);
        if( subjects.isEmpty()) {
            return Response.noContent().build();
        }
        final List<SubjectDto> subjectDtos = subjects.stream().map(s -> SubjectDto.fromSubject(uriInfo, s)).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<SubjectDto>>(subjectDtos){}).build();
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
