package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.ReviewStats;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.exceptions.DegreeNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping("/degree/{id:\\d+}")
    public ModelAndView degree(@PathVariable Long id) {
        final Optional<Degree> degree = ds.findById(id);

        if( !degree.isPresent() ){
            LOGGER.warn("Degree is not present");
            throw new DegreeNotFoundException();
        }
        long userId;
        if(!aus.isAuthenticated()) {
            userId = -1;
            LOGGER.info("User is not authenticated");
        } else {
            userId = aus.getCurrentUser().getId();
        }

        final Map<Integer, List<Subject>> infSubsByYear = ss.getInfSubsByYear(degree.get().getId());
        final List<Subject> infElectives = ss.getInfElectives(degree.get().getId());
        final Map<String, Integer> subjectProgress = us.getUserAllSubjectProgress(userId);
        final Map<String, ReviewStats> reviewStats = rs.getReviewStatMapByDegreeId(degree.get().getId());

        ModelAndView mav = new ModelAndView("degree/index");
        mav.addObject("years", infSubsByYear.keySet());
        mav.addObject("infSubsByYear", infSubsByYear);
        mav.addObject("electives", infElectives);
        mav.addObject("reviewStatistics", reviewStats);
        mav.addObject("subjectProgress", subjectProgress);
        return mav;
    }

    @RequestMapping("/")
    public ModelAndView dashboard() {
        User user;
        if(aus.isAuthenticated()) {
            user = aus.getCurrentUser();
        }
        else{
            return new ModelAndView("user/login");
        }

        Map<String, Integer> passedSubjects = us.getUserAllSubjectProgress(user.getId());


        ModelAndView mav = new ModelAndView("dashboard/dashboard");
        return mav;
    }

}
