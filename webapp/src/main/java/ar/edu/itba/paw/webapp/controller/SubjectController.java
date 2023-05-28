package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.SubjectProgress;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.exceptions.SubjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

        final SubjectProgress progress = user == null ? null : user.getSubjectProgress().get(subject);
        final int year = degreeService.findSubjectYearForDegree(subject, degree);
        final List<Review> reviews = reviewService.getAllSubjectReviews(subject, pageNum, order, dir);
        final Boolean didReview = reviewService.didUserReview(subject, user);
        final Map<Review, ReviewVote> userVotes = user == null ? null : user.getVotesByReview();

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
}
