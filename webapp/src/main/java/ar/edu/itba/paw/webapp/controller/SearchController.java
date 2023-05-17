package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class SearchController {
    private final SubjectService subjectService;
    private final ProfessorService professorService;
    private final ReviewService reviewService;

    private final UserService userService;

    private final DegreeService degreeService;

    private final AuthUserService authUserService;

    @Autowired
    public SearchController(SubjectService subjectService, ProfessorService professorService, ReviewService reviewService, UserService userService, DegreeService degreeService, AuthUserService authUserService) {
        this.subjectService = subjectService;
        this.professorService = professorService;
        this.userService = userService;
        this.reviewService = reviewService;
        this.degreeService = degreeService;
        this.authUserService = authUserService;
    }

    @RequestMapping("/search")
    public ModelAndView search(@RequestParam Map<String, String> params) {
        long userId;
        if(!authUserService.isAuthenticated())
            userId = -1;
        else
            userId = authUserService.getCurrentUser().getId();

        final List<Subject> subjects = subjectService.getByNameFiltered(params.getOrDefault("q",""), params);
        final int totalPages = subjectService.getTotalPagesForSubjects(params.getOrDefault("q",""),params);
        final Map<String, Set<String>> relevantFilters = subjectService.getRelevantFilters(params.getOrDefault("q",""),params);
        final Map<String, ReviewStats> reviewStats = reviewService.getReviewStatMapBySubjectList(subjects);
        final Map<String,Integer> subjectProgress = userService.getUserAllSubjectProgress(userId);

        ModelAndView mav = new ModelAndView("subjects/search");
        mav.addObject("subjects", subjects);
        mav.addObject("query", params.getOrDefault("q",""));
        mav.addObject("reviewStats", reviewStats);
        mav.addObject("subjectProgress", subjectProgress);
        mav.addObject("relevantFilters", relevantFilters);
        mav.addObject("totalPages",totalPages);
        mav.addObject("actualPage",subjectService.checkPageNum(params));

        return mav;
    }
}
