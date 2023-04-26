package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.ReviewStatistic;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.DegreeService;
import ar.edu.itba.paw.services.ProfessorService;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.services.SubjectService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.auth.UniAuthUser;
import ar.edu.itba.paw.webapp.exceptions.DegreeNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.SubjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.text.html.Option;
import java.util.*;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
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

    @Autowired
    public HomeController(DegreeService ds, SubjectService ss, ProfessorService ps, ReviewService rs, UserService us) {
        this.ds = ds;
        this.ss = ss;
        this.ps = ps;
        this.us = us;
        this.rs = rs;
    }

    @RequestMapping("/")
    public ModelAndView home() {
        final List<Degree> degrees = ds.getAll();
        Optional<Degree> maybeDegree = degrees.stream().findFirst();

        if( !maybeDegree.isPresent() ){
            //TODO - error
            throw new DegreeNotFoundException();
        }

        final Map<Integer, List<Subject>> infSubsByYear = ss.getInfSubsByYear(maybeDegree.get().getId());
        final List<Subject> infElectives = ss.getInfElectives(maybeDegree.get().getId());

        Set<Integer> years = infSubsByYear.keySet();

        List<Subject> subjects = new ArrayList<>();
        for(List<Subject> yearSubjects: infSubsByYear.values()){
            subjects.addAll(yearSubjects);
        }

        Map<String, ReviewStatistic> reviewStatistic = rs.getReviewStatMapBySubjectList(subjects);
        Map<String, ReviewStatistic> electivesReviewStatistic = rs.getReviewStatMapBySubjectList(infElectives);


        ModelAndView mav = new ModelAndView("home/index");
        mav.addObject("degrees", degrees);
        mav.addObject("years", years);
        mav.addObject("infSubsByYear", infSubsByYear);
        mav.addObject("electives", infElectives);
        mav.addObject("reviewStatistics", reviewStatistic);
        mav.addObject("electivesReviewStatistics", electivesReviewStatistic);
        return mav;
    }

//    @ModelAttribute("loggedUser")
//    public User loggedUser(final HttpSession session){
//        Long userId = (Long) session.getAttribute("id");
//        System.out.println(userId);
//        if(userId == null){
//            return null;
//        }
//
//        return us.findById(userId.longValue()).orElseGet(() -> null);
//    }

    @ModelAttribute("loggedUser")
    public User loggedUser(){
        Object maybeUniAuthUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if( maybeUniAuthUser.toString().equals("anonymousUser")){
            return null;
        }
        final UniAuthUser userDetails = (UniAuthUser) maybeUniAuthUser ;
        return us.getUserWithEmail(userDetails.getUsername()).orElse(null);
    }
}
