package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.ReviewStats;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.exceptions.DegreeNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class HomeController {
    private final DegreeService ds;
    private final SubjectService ss;

    private final ProfessorService ps;

    private final ReviewService rs;

    private final UserService us;

    private final AuthUserService aus;
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);


    @Autowired
    public HomeController(DegreeService ds, SubjectService ss, ProfessorService ps, ReviewService rs, UserService us, AuthUserService aus) {
        this.ds = ds;
        this.ss = ss;
        this.ps = ps;
        this.us = us;
        this.rs = rs;
        this.aus = aus;
    }

    @RequestMapping("/")
    public ModelAndView home() {
        final List<Degree> degrees = ds.getAll();
        Optional<Degree> maybeDegree = degrees.stream().findFirst();

        if( !maybeDegree.isPresent() ){
            //TODO - error
            LOGGER.warn("Degree is not present");
            throw new DegreeNotFoundException();
        }

        final Map<Integer, List<Subject>> infSubsByYear = ss.getInfSubsByYear(maybeDegree.get().getId());
        final List<Subject> infElectives = ss.getInfElectives(maybeDegree.get().getId());

        Set<Integer> years = infSubsByYear.keySet();

        List<Subject> subjects = new ArrayList<>();
        for(List<Subject> yearSubjects: infSubsByYear.values()){
            subjects.addAll(yearSubjects);
        }

        Map<String, ReviewStats> reviewStatistic = rs.getReviewStatMapBySubjectList(subjects);
        Map<String, ReviewStats> electivesReviewStatistic = rs.getReviewStatMapBySubjectList(infElectives);

        long userId;
        if(!aus.isAuthenticated()) {
            userId = -1;
            LOGGER.info("User is not authenticated");
        } else {
            userId = aus.getCurrentUser().getId();
        }
        Map<String, Integer> subjectProgress = us.getUserAllSubjectProgress(userId);

        ModelAndView mav = new ModelAndView("home/index");
        mav.addObject("degrees", degrees);
        mav.addObject("years", years);
        mav.addObject("infSubsByYear", infSubsByYear);
        mav.addObject("electives", infElectives);
        mav.addObject("reviewStatistics", reviewStatistic);
        mav.addObject("electivesReviewStatistics", electivesReviewStatistic);
        mav.addObject("subjectProgress", subjectProgress);
        return mav;
    }

}
