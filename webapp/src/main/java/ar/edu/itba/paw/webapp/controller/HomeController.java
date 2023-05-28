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
        mav.addObject("degree", degree);
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
            return new ModelAndView("redirect:/landing");
        }


        // TODO: replace with real calls
        final long userDegree = 1;           // User.getDegree
        final Map<Integer, Integer> creditsPerYear  = new HashMap<>(); // degreeService.getCreditsPerYear(userDegree)
        final Map<Integer, Integer> creditsDoneByUserPerYear = new HashMap<>(); // userService.getCreditsDoneByUserPerYear
        final int userCreditsDone = 176;    // creditsDoneByUserPerYear.stream().reduce()
        final int totalCredits = 244;       // creditsPerYear.stream().reduce()
        final double userProgressPercentage = Math.floor( ((1.0 * userCreditsDone) / totalCredits) * 100);


        // TODO: this subject should only contain the class the user is signed up to
        final List<Subject> currentUserSemester = ss.findAllThatUserCanDo(user);    // userService.getCurrentUserService

        final List<Subject> subjectsUserCanDo = ss.search("z",1);      // subjectService.getAllSubsWithClassThatUserCanDo
        final List<Subject> futureSubjects = ss.search("t",1);      // subjectService.getAllSubsUserStillCannotDo
        final List<Subject> pastSubjects = ss.search("b",1);         // subjectService.getAllSubjectsUserHasDone


        // TODO: = = = = = = = remove. Moqueo de datos = = = = = = =
        creditsPerYear.put(1,48);
        creditsPerYear.put(2,50);
        creditsPerYear.put(3,45);
        creditsPerYear.put(4,45);
        creditsPerYear.put(5,20);
        creditsPerYear.put(-1,27);

        creditsDoneByUserPerYear.put(1,48);
        creditsDoneByUserPerYear.put(2,50);
        creditsDoneByUserPerYear.put(3,45);
        creditsDoneByUserPerYear.put(4,30);
        creditsDoneByUserPerYear.put(5,0);
        creditsDoneByUserPerYear.put(-1,5);

        currentUserSemester.removeIf(e->e.getCredits() <= 3);
        for(Subject s : currentUserSemester){

            SubjectClass only =  s.getClasses().stream().findFirst().get();
            s.getClasses().clear();
            s.getClasses().add(only);
        }

//        currentUserSemester.clear();
//        pastSubjects.clear();
        // TODO: = = = = = = = remove. = = = = = = =

        ModelAndView mav = new ModelAndView("dashboard/dashboard");
        mav.addObject("userProgressPercentage",userProgressPercentage);
        mav.addObject("creditsPerYear",creditsPerYear);
        mav.addObject("creditsDoneByUserPerYear",creditsDoneByUserPerYear);
        mav.addObject("userCreditsDone",userCreditsDone);
        mav.addObject("totalCredits",totalCredits);
        mav.addObject("currentUserSemester",currentUserSemester);
        mav.addObject("subjectsUserCanDo",subjectsUserCanDo);
        mav.addObject("futureSubjects",futureSubjects);
        mav.addObject("pastSubjects",pastSubjects);
        return mav;
    }

    @RequestMapping("/landing")
    public ModelAndView landing() {
        return new ModelAndView("landing");
    }

}

