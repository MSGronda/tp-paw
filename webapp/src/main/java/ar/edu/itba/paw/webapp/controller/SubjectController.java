package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.services.AuthUserService;
import ar.edu.itba.paw.services.DegreeService;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

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
            @RequestParam(name = "pageNum", defaultValue = "1") final int page,
            @RequestParam(defaultValue = "name") final String order,
            @RequestParam(defaultValue = "asc") final String dir
    ) {
        final Subject subject = subjectService.findById(id).orElseThrow(SubjectNotFoundException::new);
        final User user = authUserService.getCurrentUser();

        //Todo?: find votes for current subject and page only
        final Map<Review, ReviewVote> userVotes = user.getVotesByReview();

        final ModelAndView mav = new ModelAndView("subjects/subject_info");
        mav.addObject("totalPages", reviewService.getTotalPagesForSubjectReviews(subject));
        mav.addObject("reviews", reviewService.getAllSubjectReviews(subject, page, order, dir));
        mav.addObject("year", degreeService.findSubjectYearForDegree(subject, user.getDegree()));
        mav.addObject("didReview", reviewService.didUserReview(subject, user));
        mav.addObject("progress", user.getSubjectProgress(subject));
        mav.addObject("user", user);
        mav.addObject("subject", subject);
        mav.addObject("userVotes", userVotes);
        mav.addObject("currentPage", page);
        mav.addObject("order", order);
        mav.addObject("dir", dir);

        return mav;
    }
}
