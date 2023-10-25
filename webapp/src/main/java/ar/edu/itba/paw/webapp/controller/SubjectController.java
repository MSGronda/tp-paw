package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.*;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.dto.SubjectDto;
import ar.edu.itba.paw.webapp.form.SubjectForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Path("subjects")
@Component
public class SubjectController {

    private final SubjectService subjectService;
    private final ReviewService reviewService;
    private final DegreeService degreeService;
    private final AuthUserService authUserService;
    private final ProfessorService professorService;
    private final UserService userService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public SubjectController(
            final SubjectService subjectService,
            final ReviewService reviewService,
            final DegreeService degreeService,
            final AuthUserService authUserService,
            final ProfessorService professorService,
            UserService userService) {
        this.subjectService = subjectService;
        this.reviewService = reviewService;
        this.degreeService = degreeService;
        this.authUserService = authUserService;
        this.professorService = professorService;
        this.userService = userService;
    }

//    @RequestMapping("/subject/{id:\\d+\\.\\d+}")
//    public ModelAndView subjectInfo(
//            @PathVariable final String id,
//            @RequestParam(name = "page", defaultValue = "1") final int page,
//            @RequestParam(defaultValue = "name") final String order,
//            @RequestParam(defaultValue = "asc") final String dir
//    ) {
//        final Subject subject = subjectService.findById(id).orElseThrow(SubjectNotFoundException::new);
//        final User user = authUserService.getCurrentUser();
//
//        final ModelAndView mav = new ModelAndView("subjects/subject_info");
//        mav.addObject("totalPages", reviewService.getTotalPagesForSubjectReviews(subject));
//        mav.addObject("reviews", reviewService.getAllSubjectReviews(subject, page, order, dir));
//        mav.addObject("degree", degreeService.findParentDegree(subject, user));
//        mav.addObject("year", degreeService.findSubjectYearForParentDegree(subject, user));
//        mav.addObject("didReview", reviewService.didUserReview(subject, user));
//        mav.addObject("progress", user.getSubjectProgress(subject));
//        mav.addObject("user", user);
//        mav.addObject("subject", subject);
//        mav.addObject("currentPage", page);
//        mav.addObject("order", order);
//        mav.addObject("dir", dir);
//        return mav;
//    }
//
//    @RequestMapping(value = "/create-subject", method = {RequestMethod.GET})
//    public ModelAndView createSubjectForm(@ModelAttribute("subjectForm") final SubjectForm subjectForm) {
//        final List<Subject> subjects = subjectService.getAll();
//        final List<Professor> professors = professorService.getAll();
//        final List<Degree> degrees = degreeService.getAll();
//
//        final ModelAndView mav = new ModelAndView("moderator-tools/createSubject");
//        mav.addObject("subjects", subjects);
//        mav.addObject("professors", professors);
//        mav.addObject("degrees", degrees);
//        return mav;
//    }
//
//    @RequestMapping(value = "/create-subject", method = {RequestMethod.POST} )
//    public ModelAndView createSubject(
//            @Valid @ModelAttribute("subjectForm") final SubjectForm subjectForm,
//            final BindingResult errors
//    ) {
//        if(errors.hasErrors()) {
//            return createSubjectForm(subjectForm);
//        }
//
//        final Subject newSub;
//        try{
//            newSub = subjectService.create(Subject.builder()
//                    .id(subjectForm.getId())
//                    .name(subjectForm.getName())
//                    .department(subjectForm.getDepartment())
//                    .credits(subjectForm.getCredits()),
//                    subjectForm.getDegreeIds(),
//                    subjectForm.getSemesters(),
//                    subjectForm.getRequirementIds(),
//                    subjectForm.getProfessors(),
//                    subjectForm.getClassCodes(),
//                    subjectForm.getClassProfessors(),
//                    subjectForm.getClassDays(),
//                    subjectForm.getClassStartTimes(),
//                    subjectForm.getClassEndTimes(),
//                    subjectForm.getClassBuildings(),
//                    subjectForm.getClassRooms(),
//                    subjectForm.getClassModes()
//            );
//        } catch (SubjectIdAlreadyExistsException e) {
//            final ModelAndView mav = createSubjectForm(subjectForm);
//            mav.addObject("subjectCodeRepeated", true);
//            return mav;
//        }
//        return new ModelAndView("redirect:/subject/"+newSub.getId());
//    }
//
//    @RequestMapping("/subject/{id:\\d+\\.\\d+}/delete-subject")
//    public ModelAndView deleteSubject(
//            @PathVariable final String id
//    ) throws UnauthorizedException, SubjectNotFoundException {
//
//        subjectService.delete(authUserService.getCurrentUser(), id);
//        return new ModelAndView("redirect:/");
//    }
//
//    @RequestMapping(value = "/subject/{id:\\d+\\.\\d+}/edit", method = {RequestMethod.GET})
//    public ModelAndView editSubjectForm(
//            @PathVariable final String id,
//            @ModelAttribute("subjectForm") final EditSubjectForm editSubjectForm
//    ) {
//        final Subject subject = subjectService.findById(id).orElseThrow(SubjectNotFoundException::new);
//        final List<Subject> subjects = subjectService.getAll();
//        final List<Professor> professors = professorService.getAll();
//        final List<Degree> degrees = degreeService.getAll();
//
//        final ModelAndView mav = new ModelAndView("moderator-tools/editSubject");
//        mav.addObject("subject", subject);
//        mav.addObject("allSubjects", subjects);
//        mav.addObject("professors", professors);
//        mav.addObject("degrees", degrees);
//        return mav;
//    }
//
//    @RequestMapping(value = "/subject/{id:\\d+\\.\\d+}/edit", method = {RequestMethod.POST})
//    public ModelAndView editSubject(
//            @PathVariable final String id,
//            @Valid @ModelAttribute("editSubjectForm") final EditSubjectForm editSubjectForm,
//            final BindingResult errors
//    ) {
//        if(errors.hasErrors()) {
//            return editSubjectForm(id, editSubjectForm);
//        }
//
//        subjectService.edit(
//                id,
//                editSubjectForm.getCredits(),
//                editSubjectForm.getDegreeIds(),
//                editSubjectForm.getSemesters(),
//                editSubjectForm.getRequirementIds(),
//                editSubjectForm.getProfessors(),
//                editSubjectForm.getClassIds(),
//                editSubjectForm.getClassCodes(),
//                editSubjectForm.getClassProfessors(),
//                editSubjectForm.getClassDays(),
//                editSubjectForm.getClassStartTimes(),
//                editSubjectForm.getClassEndTimes(),
//                editSubjectForm.getClassBuildings(),
//                editSubjectForm.getClassRooms(),
//                editSubjectForm.getClassModes()
//        );
//        return new ModelAndView("redirect:/subject/"+id);
//    }



    // = = = = = = = = = = = API = = = = = = = = = = = = =
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

        final Optional<User> user = userService.getRelevantUser(available, unLockable, done, future, plan);

        if(!user.isPresent()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final List<Subject> subs;
        try{
            subs = subjectService.get(
                    user.get(),
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
        } catch (DegreeNotFoundException | InvalidPageNumberException | SemesterNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final List<SubjectDto> subjectsDtos = subs.stream().map(subject -> SubjectDto.fromSubject(uriInfo, subject)).collect(Collectors.toList());

        if(subjectsDtos.isEmpty())
            return Response.noContent().build();

        return Response.ok(new GenericEntity<List<SubjectDto>>(subjectsDtos){}).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSubject(@Valid @ModelAttribute("subjectForm") final SubjectForm subjectForm){
        Subject newSub;
        try{
            newSub = subjectService.create(Subject.builder()
                            .id(subjectForm.getId())
                            .name(subjectForm.getName())
                            .department(subjectForm.getDepartment())
                            .credits(subjectForm.getCredits()),
                    subjectForm.getDegreeIds(),
                    subjectForm.getSemesters(),
                    subjectForm.getRequirementIds(),
                    subjectForm.getProfessors(),
                    subjectForm.getClassCodes(),
                    subjectForm.getClassProfessors(),
                    subjectForm.getClassDays(),
                    subjectForm.getClassStartTimes(),
                    subjectForm.getClassEndTimes(),
                    subjectForm.getClassBuildings(),
                    subjectForm.getClassRooms(),
                    subjectForm.getClassModes()
            );
        } catch (SubjectIdAlreadyExistsException e){        // No se si conflict es el status code que corresponde
            return Response.status(Response.Status.CONFLICT.getStatusCode()).build();
        }
        return Response.status(Response.Status.CREATED.getStatusCode()).build();
    }
}
