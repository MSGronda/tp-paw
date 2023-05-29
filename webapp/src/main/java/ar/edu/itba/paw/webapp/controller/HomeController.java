package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.SubjectProgress;
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
import java.util.Map;

@Controller
public class HomeController {
    private final DegreeService ds;
    private final SubjectService ss;

    private final UserService us;

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    private final AuthUserService aus;

    @Autowired
    public HomeController(DegreeService ds, SubjectService ss, ProfessorService ps, UserService us, AuthUserService aus) {
        this.ds = ds;
        this.aus = aus;
        this.ss = ss;
        this.us = us;
    }

    @RequestMapping("/degree/{id:\\d+}")
    public ModelAndView degree(@PathVariable Long id) {
        final Optional<Degree> degree = ds.findById(id);

        if( !degree.isPresent() ){
            LOGGER.warn("Degree is not present");
            throw new DegreeNotFoundException();
        }
        User user;
        if(!aus.isAuthenticated()) {
            user = null;
            LOGGER.info("User is not authenticated");
        } else {
            user = aus.getCurrentUser();
        }

        final Map<String, SubjectProgress> progress = user == null ? new HashMap<>() : user.getSubjectProgress();

        ModelAndView mav = new ModelAndView("degree/index");
        mav.addObject("degree", degree.get());
        mav.addObject("subjectProgress", progress);
        return mav;
    }

    @RequestMapping("/")
    public ModelAndView dashboard() {
        User user;
        if(aus.isAuthenticated()) {
            user = aus.getCurrentUser();
        }
        else{
            return new ModelAndView("landing");
        }

        final long userDegree = 1;           // User.getDegree

        final Optional<Degree> maybeDegree = ds.findById(userDegree);
        if(!maybeDegree.isPresent()){
            throw new DegreeNotFoundException();
        }
        Degree degree = maybeDegree.get();
        final List<Subject> subjectsUserCanDo = ss.findAllThatUserCanDo(user);
        final List<Subject> futureSubjects = ss.findAllThatUserHasNotDone(user);
        final List<Subject> pastSubjects = ss.findAllThatUserHasDone(user);
        final double userProgressPercentage = Math.floor( ((1.0 * user.getCreditsDone()) / degree.getTotalCredits()) * 100);
        final Map<Integer, Double> percentageCompletionByYear = us.getUserProgressionPerYear( degree,  user); // userService.getCreditsDoneByUserPerYear

        // TODO: replace with real calls
        // TODO: this subject should only contain the class the user is signed up to
        final List<Subject> currentUserSemester = new ArrayList<>();    // userService.getCurrentUserService


        ModelAndView mav = new ModelAndView("dashboard/dashboard");
        mav.addObject("degree",degree);
        mav.addObject("user",user);
        mav.addObject("subjectsUserCanDo",subjectsUserCanDo);
        mav.addObject("futureSubjects",futureSubjects);
        mav.addObject("pastSubjects",pastSubjects);

        mav.addObject("userProgressPercentage",userProgressPercentage);

        mav.addObject("percentageCompletionByYear",percentageCompletionByYear);
        mav.addObject("currentUserSemester",currentUserSemester);
        return mav;
    }
}

