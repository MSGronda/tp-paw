package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.ProfessorService;
import ar.edu.itba.paw.services.SubjectService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.auth.UniAuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController {
    private final SubjectService subjectService;
    private final ProfessorService professorService;

    private final UserService userService;

    @Autowired
    public SearchController(SubjectService subjectService, ProfessorService professorService, UserService userService) {
        this.subjectService = subjectService;
        this.professorService = professorService;
        this.userService = userService;
    }

    @RequestMapping("/search/{name}")
    public ModelAndView search(@PathVariable String name, @RequestParam Map<String, String> params) {

        final List<Subject> subjects;

        if(params.containsKey("ob")) {
            String ob = params.get("ob");
            params.remove("ob");
            subjects = subjectService.getByNameFiltered(name, params, ob);
        }
        else
            subjects = subjectService.getByNameFiltered(name, params, "subname");

        final Map<Subject, List<Professor>> profs = new HashMap<>();

        for(Subject sub : subjects) {
            profs.put(sub, professorService.getAllBySubject(sub.getId()));
        }

        ModelAndView mav = new ModelAndView("subjects/search");
        mav.addObject("subjects", subjects);
        mav.addObject("query", name);
        mav.addObject("profs",profs);

        return mav;
    }

    @ModelAttribute("loggedUser")
    public User loggedUser(){
        Object maybeUniAuthUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if( maybeUniAuthUser.toString().equals("anonymousUser")){
            return null;
        }
        final UniAuthUser userDetails = (UniAuthUser) maybeUniAuthUser ;
        return userService.getUserWithEmail(userDetails.getUsername()).orElse(null);
    }
}
