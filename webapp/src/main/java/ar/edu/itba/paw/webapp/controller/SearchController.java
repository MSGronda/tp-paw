package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.auth.UniAuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
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

        final List<Subject> subjects = subjectService.getByNameFiltered(params.getOrDefault("q",""), params);
        final Map<String, Set<String>> relevantFilters = subjectService.getRelevantFilters(subjects);

        Map<String, ReviewStatistic> reviewStats = reviewService.getReviewStatMapBySubjectList(subjects);
        final int totalPages = subjectService.getTotalPagesForSubjects(params.getOrDefault("q",""),params);
        long userId;
        if(!authUserService.isAuthenticated())
            userId = -1;
        else
            userId = authUserService.getCurrentUser().getId();

        Map<String,Integer> subjectProgress = userService.getUserAllSubjectProgress(userId);

        ModelAndView mav = new ModelAndView("subjects/search");
        mav.addObject("subjects", subjects);
        mav.addObject("query", params.getOrDefault("q",""));
        mav.addObject("reviewStats", reviewStats);
        mav.addObject("subjectProgress", subjectProgress);
        mav.addObject("relevantFilters", relevantFilters);
        mav.addObject("totalPages",totalPages);
        if(params.isEmpty()){
            mav.addObject("actualPage",1);
        } else {
            if(!params.getOrDefault("pageNum","1").matches("[0-9]+")){
                mav.addObject("actualPage",1);
            } else {
                int actualPage = Integer.parseInt(params.getOrDefault("pageNum","0")) + 1;
                mav.addObject("actualPage",actualPage);
            }
        }

        return mav;
    }

    @ModelAttribute("degrees")
    public List<Degree> degrees(){
        return degreeService.getAll();
    }
}
