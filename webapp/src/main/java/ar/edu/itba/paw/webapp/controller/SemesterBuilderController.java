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
    private final AuthUserService authUserService;
    private final SubjectService subjectService;

    @Autowired
    public SemesterBuilderController(AuthUserService authUserService, SubjectService subjectService) {
        this.authUserService = authUserService;
        this.subjectService = subjectService;
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

        List<Subject> availableSubjects = subjectService.findAllThatUserCanDo(user);

        mav.addObject("availableSubjects", availableSubjects);

        return mav;
    }
}
