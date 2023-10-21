package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.AuthUserService;
import ar.edu.itba.paw.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    private final SubjectService subjectService;
    private final AuthUserService authUserService;

    @Autowired
    public HomeController(
            final SubjectService subjectService,
            final AuthUserService authUserService
    ) {
        this.authUserService = authUserService;
        this.subjectService = subjectService;
    }

//    @RequestMapping("/")
//    public ModelAndView dashboard() {
//        if (!authUserService.isAuthenticated()) return new ModelAndView("landing");
//
//        final User user = authUserService.getCurrentUser();
//        final Degree degree = user.getDegree();
//
//        ModelAndView mav = new ModelAndView("dashboard/dashboard");
//        mav.addObject("degree", degree);
//        mav.addObject("user", user);
//        mav.addObject("subjectsUserCanDo", subjectService.findAllThatUserCanDo(user));
//        mav.addObject("futureSubjects", subjectService.findAllThatUserHasNotDone(user));
//        mav.addObject("pastSubjects", subjectService.findAllThatUserHasDone(user));
//        mav.addObject("userProgressPercentage", user.getTotalProgressPercentageForDegree());
//        mav.addObject("userCreditsDoneForDegree", user.getCreditsDoneForDegree());
//        return mav;
//    }
}

