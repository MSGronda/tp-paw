package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.exceptions.SubjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

import static java.lang.Long.parseLong;

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
    public ModelAndView subjectInfo(@PathVariable String id,@RequestParam Map<String, String> param) {
        final Optional<Subject> maybeSubject = subjectService.findById(id);
        if(!maybeSubject.isPresent()) {
            throw new SubjectNotFoundException("No subject with given id");
        }
        long userId;
        User user;
        if( authUserService.isAuthenticated()){
            user = authUserService.getCurrentUser();
            userId = user.getId();
        }else{
            user = null;
            userId = -1;
        }

        final Subject subject = maybeSubject.get();
        final int totalPages = reviewService.getTotalPagesForReviews(id);

        final int page = Integer.parseInt(param.getOrDefault("pageNum", "1"));
        if(page < 1 || page > totalPages) return new ModelAndView("redirect:/404");

        final String order = param.getOrDefault("order", "name");
        final String dir = param.getOrDefault("dir", "desc");

        //TODO: get degree from user
        final Degree degree = degreeService.findById(1L).orElseThrow(IllegalStateException::new);
        final Integer progress = user == null ? 0 : user.getSubjectProgress().get(subject.getId());
        final int year = degreeService.findSubjectYearForDegree(subject, degree);
        final List<Review> reviews = reviewService.getAllSubjectReviewsWithUsername(id,param);
        final Boolean didReview = reviewService.didUserReview(reviews, user);
        final Map<Long, Integer> userVotes = reviewService.userReviewVoteByIdSubAndIdUser(id, userId);

        ModelAndView mav = new ModelAndView("subjects/subject_info");
        mav.addObject("subject", subject);
        mav.addObject("progress", progress);
        mav.addObject("totalPages",totalPages);
        mav.addObject("reviews", reviews);
        mav.addObject("year", year);
        mav.addObject("didReview", didReview);
        mav.addObject("userVotes", userVotes);
        mav.addObject("currentPage", page);
        mav.addObject("order", order);
        mav.addObject("dir", dir);

        return mav;
    }
}
