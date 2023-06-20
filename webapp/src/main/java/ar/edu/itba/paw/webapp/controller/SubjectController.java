package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.SubjectProgress;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.services.exceptions.SubjectIdAlreadyExistsException;
import ar.edu.itba.paw.webapp.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.webapp.form.ProfessorForm;
import ar.edu.itba.paw.webapp.form.SubjectForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.*;

@Controller
public class SubjectController {
    private final SubjectService subjectService;
    private final ReviewService reviewService;
    private final DegreeService degreeService;
    private final AuthUserService authUserService;
    private final ProfessorService professorService;

    @Autowired
    public SubjectController(
            SubjectService subjectService,
            ReviewService reviewService,
            DegreeService degreeService,
            AuthUserService authUserService,
            ProfessorService professorService
    ) {
        this.subjectService = subjectService;
        this.reviewService = reviewService;
        this.degreeService = degreeService;
        this.authUserService = authUserService;
        this.professorService = professorService;
    }

    @RequestMapping("/subject/{id:\\d+\\.\\d+}")
    public ModelAndView subjectInfo(
            @PathVariable final String id,
            @RequestParam(required = false, defaultValue = "1") final int pageNum,
            @RequestParam(required = false, defaultValue = "name") final String order,
            @RequestParam(required = false, defaultValue = "asc") final String dir
    ) {
        final Optional<Subject> maybeSubject = subjectService.findById(id);
        if(!maybeSubject.isPresent()) {
            throw new SubjectNotFoundException("No subject with given id");
        }

        final User user;
        if( authUserService.isAuthenticated()){
            user = authUserService.getCurrentUser();
        } else {
            user = null;
        }

        final Subject subject = maybeSubject.get();
        final int totalPages = reviewService.getTotalPagesForSubjectReviews(subject);

        if(pageNum < 1 || pageNum > totalPages) return new ModelAndView("redirect:/404");

        //TODO: get degree from user
        final Degree degree = degreeService.findById(1L).orElseThrow(IllegalStateException::new);

        final SubjectProgress progress = user == null ? SubjectProgress.PENDING : user.getSubjectProgress().getOrDefault(subject.getId(),SubjectProgress.PENDING);
        final int year = degreeService.findSubjectYearForDegree(subject, degree);
        final List<Review> reviews = reviewService.getAllSubjectReviews(subject, pageNum, order, dir);
        final Boolean didReview = reviewService.didUserReview(subject, user);
        final Map<Review, ReviewVote> userVotes = user == null ? new HashMap<>() : user.getVotesByReview();

        ModelAndView mav = new ModelAndView("subjects/subject_info");
        mav.addObject("user", user);
        mav.addObject("subject", subject);
        mav.addObject("progress", progress);
        mav.addObject("totalPages",totalPages);
        mav.addObject("reviews", reviews);
        mav.addObject("year", year);
        mav.addObject("didReview", didReview);
        mav.addObject("userVotes", userVotes);
        mav.addObject("currentPage", pageNum);
        mav.addObject("order", order);
        mav.addObject("dir", dir);

        return mav;
    }

    @RequestMapping(value = "/create-subject", method = {RequestMethod.GET} )
    public ModelAndView createSubjectForm(@ModelAttribute("subjectForm") final SubjectForm subjectForm) {

        ModelAndView mav = new ModelAndView("moderator-tools/createSubject");
        List<Subject> subjects = subjectService.getAll();
        List<Professor> professors = professorService.getAll();
        List<Degree> degrees = degreeService.getAll();
        mav.addObject("subjects", subjects);
        mav.addObject("professors", professors);
        mav.addObject("degrees", degrees);
        return mav;
    }
    @RequestMapping(value = "/create-subject", method = {RequestMethod.POST} )
    public ModelAndView createSubject(@Valid @ModelAttribute("subjectForm") final SubjectForm subjectForm,
                                      final BindingResult errors) {
        System.out.println("ID " + subjectForm.getId() + "\n");
        System.out.println("Name " + subjectForm.getName() + "\n");
        System.out.println("Department " + subjectForm.getDepartment() + "\n");
        System.out.println("credits " + subjectForm.getCredits() + "\n");
        System.out.println("Degrees " + subjectForm.getDegreeIds() + "\n");
        System.out.println("semesters " + subjectForm.getSemesters() + "\n");
        System.out.println("requirements " + subjectForm.getRequirementIds() + "\n");
        System.out.println("Professors " + subjectForm.getProfessors() + "\n");
        System.out.println("Class Codes " + subjectForm.getClassCodes() + "\n");
        System.out.println("Class Professors " + subjectForm.getClassProfessors() + "\n");
        System.out.println("Class days " + subjectForm.getClassDays() + "\n");
        System.out.println("Class start times " + subjectForm.getClassStartTimes() + "\n");
        System.out.println("Class End TImes " + subjectForm.getClassEndTimes() + "\n");
        System.out.println("class buildings " + subjectForm.getClassBuildings() + "\n");
        System.out.println("class rooms " + subjectForm.getClassRooms() + "\n");
        System.out.println("class modes " + subjectForm.getClassModes() + "\n");


        if(errors.hasErrors()) {
            return createSubjectForm(subjectForm);
        }
        Subject newSub;
//        try{
//            newSub = subjectService.create(Subject.builder()
//                    .id(subjectForm.getId())
//                    .name(subjectForm.getName())
//                    .department(subjectForm.getDepartment())
//                    .credits(subjectForm.getCredits())
//                    .build()
//            );
//        } catch (SubjectIdAlreadyExistsException e) {
//            //ToDo indicar que el id ya que existe en el form
//        }
//
//
//        return new ModelAndView("redirect:/subject/{subjectId:\\d+\\.\\d+}/addProfessor");
        return new ModelAndView("redirect:/");
    }
    @RequestMapping(value = "/subject/{subjectId:\\d+\\.\\d+}/add-professors", method = {RequestMethod.GET} )
    public ModelAndView addProfessorToSubjectForm(@PathVariable final String subjectId,
                                                  @ModelAttribute("professorForm") final SubjectForm subjectForm,
                                                  final BindingResult errors) {

        if(errors.hasErrors()) {
            return createSubjectForm(subjectForm);
        }

        ModelAndView mav = new ModelAndView("moderator-tools/addProfessors");

        final Optional<Subject> maybeSubject = subjectService.findById(subjectId);
        if(!maybeSubject.isPresent()) {
            throw new SubjectNotFoundException("No subject with given id");
        }
        mav.addObject("subject",maybeSubject.get());
        mav.addObject("professors",professorService.getAll());
        return mav;
    }
    @RequestMapping(value = "/subject/{subjectId:\\d+\\.\\d+}/add-professors", method = {RequestMethod.POST} )
    public ModelAndView addProfessorToSubject() {
        return new ModelAndView("redirect:/subject/{subjectId:\\d+\\.\\d+}/addClasses");
    }
    @RequestMapping(value = "/create-professor", method = {RequestMethod.GET} )
    public ModelAndView createProfessorForm(@ModelAttribute("professorForm") final ProfessorForm professorForm) {
        return new ModelAndView("moderator-tools/createProfessor");
    }
    @RequestMapping(value = "/create-professor", method = {RequestMethod.POST} )
    public ModelAndView createProfessor(@ModelAttribute("professorForm") final ProfessorForm professorForm,
                                        final BindingResult errors) {
        if(errors.hasErrors()) {
            return createProfessorForm(professorForm);
        }

        professorService.create(new Professor(professorForm.getName()));

        return new ModelAndView("redirect:/subject/{subjectId:\\d+\\.\\d+}/addProfessor");
    }
    @RequestMapping(value = "/subject/{subjectId:\\d+\\.\\d+}/add-classes", method = {RequestMethod.GET} )
    public ModelAndView addClassesToSubjectForm() {
        return new ModelAndView("moderator-tools/addClasses");
    }
    @RequestMapping(value = "/subject/{subjectId:\\d+\\.\\d+}/add-classes", method = {RequestMethod.POST} )
    public ModelAndView addClassesToSubject() {
        return new ModelAndView("redirect:/subject/{subjectId:\\d+\\.\\d+}");
    }


    @RequestMapping("/edit-subject")
    public ModelAndView editSubject() {
        return new ModelAndView("moderator-tools/editSubject");
    }
}
