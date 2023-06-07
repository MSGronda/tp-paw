package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.SubjectProgress;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.webapp.form.SubjectForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
public class SubjectController {
    private final SubjectService subjectService;
    private final ReviewService reviewService;
    private final DegreeService degreeService;
    private final AuthUserService authUserService;

    @Autowired
    public SubjectController(
            SubjectService subjectService,
            ReviewService reviewService,
            DegreeService degreeService,
            AuthUserService authUserService
    ) {
        this.subjectService = subjectService;
        this.reviewService = reviewService;
        this.degreeService = degreeService;
        this.authUserService = authUserService;
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
        mav.addObject("subjects", subjects);
        return mav;
    }
    @RequestMapping(value = "/create-subject", method = {RequestMethod.POST} )
    public ModelAndView createSubject(@ModelAttribute("subjectForm") final SubjectForm subjectForm,
                                      final BindingResult errors) {
        if(errors.hasErrors()) {
            return createSubjectForm(subjectForm);
        }
        return new ModelAndView("redirect:/subject/{subjectId:\\d+\\.\\d+}/addProfessor");
    }
    @RequestMapping(value = "/subject/{subjectId:\\d+\\.\\d+}/add-professors", method = {RequestMethod.GET} )
    public ModelAndView addProfessorToSubjectForm() {
        return new ModelAndView("moderator-tools/addProfessors");
    }
    @RequestMapping(value = "/subject/{subjectId:\\d+\\.\\d+}/add-professors", method = {RequestMethod.POST} )
    public ModelAndView addProfessorToSubject() {
        return new ModelAndView("redirect:/subject/{subjectId:\\d+\\.\\d+}/addClasses");
    }
    @RequestMapping(value = "/create-professor", method = {RequestMethod.GET} )
    public ModelAndView createProfessorForm() {
        return new ModelAndView("moderator-tools/createProfessor");
    }
    @RequestMapping(value = "/create-professor", method = {RequestMethod.POST} )
    public ModelAndView createProfessor() {
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
