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

    @Autowired
    public SemesterBuilderController( SubjectClassService subjectClassService, AuthUserService authUserService ) {
        this.subjectClassService = subjectClassService;
        this.authUserService = authUserService;
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

        mav.addObject("availableSubjects", availableSubjects);

        return mav;
    }


}
