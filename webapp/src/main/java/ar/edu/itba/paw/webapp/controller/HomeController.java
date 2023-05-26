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
    private final SubjectClassService scs;
    private final ProfessorService ps;

    private final ReviewService rs;

    private final UserService us;

    private final AuthUserService aus;
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);


    @Autowired
    public HomeController(DegreeService ds, SubjectService ss, ProfessorService ps, ReviewService rs, SubjectClassService scs, UserService us, AuthUserService aus) {
        this.ds = ds;
        this.ss = ss;
        this.ps = ps;
        this.us = us;
        this.rs = rs;
        this.aus = aus;
        this.scs = scs;
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


        // TODO: replace with real calls
        final long userDegree = 1;           // User.getDegree
        final Map<Integer, Integer> creditsPerYear  = new HashMap<>(); // degreeService.getCreditsPerYear(userDegree)
        final Map<Integer, Integer> creditsDoneByUserPerYear = new HashMap<>(); // userService.getCreditsDoneByUserPerYear
        final int userCreditsDone = 176;    // creditsDoneByUserPerYear.stream().reduce()
        final int totalCredits = 244;       // creditsPerYear.stream().reduce()
        final double userProgressPercentage = Math.floor( ((1.0 * userCreditsDone) / totalCredits) * 100);

        final List<Subject> currentUserSemester = scs.getAllSubsWithClassThatUserCanDo(user.getId());    // userService.getCurrentUserService
        final List<Subject> subjectsUserCanDo = ss.getByName("z");      // subjectService.getAllSubsWithClassThatUserCanDo
        final List<Subject> futureSubjects = ss.getByName("t");      // subjectService.getAllSubsUserStillCannotDo
        final List<Subject> pastSubjects = ss.getByName("b");         // subjectService.getAllSubjectsUserHasDone


        // TODO: remove. Moqueo de datos
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

        // TODO: remove. = = = = = = =
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

}
