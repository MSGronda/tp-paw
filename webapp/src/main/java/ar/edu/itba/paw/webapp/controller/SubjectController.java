package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.*;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.dto.SubjectDto;
import ar.edu.itba.paw.webapp.form.SubjectClassTimeForm;
import ar.edu.itba.paw.webapp.form.SubjectForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;


@Path("subjects")
@Component
public class SubjectController {

    private final SubjectService subjectService;
    private final AuthUserService authUserService;
    private final UserService userService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public SubjectController(
            final SubjectService subjectService,
            final AuthUserService authUserService,
            final UserService userService
    ) {
        this.subjectService = subjectService;
        this.authUserService = authUserService;
        this.userService = userService;
    }

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

        return Response.ok(new GenericEntity<List<SubjectDto>>(subjectsDtos){}).build();
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

        subjectForm.getSubjectClasses().forEach(classForm -> subjectService.addClass(
                newSub,
                classForm.getCode(),
                classForm.getProfessors(),
                classForm.getClassTimes().stream().map(SubjectClassTimeForm::getDay).collect(Collectors.toList()),
                classForm.getClassTimes().stream().map(SubjectClassTimeForm::getStartTime).collect(Collectors.toList()),
                classForm.getClassTimes().stream().map(SubjectClassTimeForm::getEndTime).collect(Collectors.toList()),
                classForm.getClassTimes().stream().map(SubjectClassTimeForm::getLocation).collect(Collectors.toList()),
                classForm.getClassTimes().stream().map(SubjectClassTimeForm::getBuilding).collect(Collectors.toList()),
                classForm.getClassTimes().stream().map(SubjectClassTimeForm::getMode).collect(Collectors.toList())
        ));

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

//    @PUT
//    @Path("/{id}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response editSubject(
//            @PathParam("id") final String subjectId,
//            @Valid @ModelAttribute("subjectForm") final SubjectForm subjectForm
//        ){
//        subjectService.edit(
//                subjectForm.getId(),
//                subjectForm.getCredits(),
//                subjectForm.getDegreeIds(),
//                subjectForm.getSemesters(),
//                subjectForm.getRequirementIds(),
//                subjectForm.getProfessors(),
//                subjectForm.getClassCodes(),
//                subjectForm.getClassCodes(),        // EEEEEEE ?????
//                subjectForm.getClassProfessors(),
//                subjectForm.getClassDays(),
//                subjectForm.getClassStartTimes(),
//                subjectForm.getClassEndTimes(),
//                subjectForm.getClassBuildings(),
//                subjectForm.getClassRooms(),
//                subjectForm.getClassModes()
//        );
//        return Response.ok().build();
//    }
}
