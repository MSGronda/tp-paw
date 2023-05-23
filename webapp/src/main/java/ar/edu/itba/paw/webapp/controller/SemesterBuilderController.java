package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

import static java.lang.Long.parseLong;

@Controller
public class SemesterBuilderController {

    private final SubjectClassService subjectClassService;
    private final AuthUserService authUserService;
    private final ReviewService reviewService;

    @Autowired
    public SemesterBuilderController( SubjectClassService subjectClassService, ReviewService reviewService, AuthUserService authUserService ) {
        this.subjectClassService = subjectClassService;
        this.authUserService = authUserService;
        this.reviewService = reviewService;
    }

    @RequestMapping("/builder")
    public ModelAndView subjectInfo() {
        User user;
        if(authUserService.isAuthenticated()) {
           user = authUserService.getCurrentUser();
        }
        else{
            return new ModelAndView(":/login/");
        }

        ModelAndView mav = new ModelAndView("builder/semester-builder");

        List<Subject> availableSubjects = subjectClassService.getAllSubsWithClassThatUserCanDo(user.getId());
        Map<String, ReviewStats> availableSubjectsStatistics = reviewService.getReviewStatMapBySubjectList(availableSubjects);


        mav.addObject("availableSubjects", availableSubjects);
        mav.addObject("availableSubjectsStatistics", availableSubjectsStatistics);

        return mav;
    }


}
