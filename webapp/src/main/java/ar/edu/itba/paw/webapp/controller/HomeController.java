package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Degree;
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
import java.util.Map;

@Controller
public class HomeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    private final DegreeService ds;
    private final AuthUserService aus;

    @Autowired
    public HomeController(DegreeService ds, AuthUserService aus) {
        this.ds = ds;
        this.aus = aus;
    }

    @RequestMapping("/degree/{degreeName}")
    public ModelAndView degree(@PathVariable String degreeName) {
        final User user;
        if(!aus.isAuthenticated()) {
            user = null;
            LOGGER.info("User is not authenticated");
        } else {
            user = aus.getCurrentUser();
        }

        final Optional<Degree> maybeDegree = ds.findByName(degreeName);

        if(!maybeDegree.isPresent()) {
            LOGGER.warn("Degree is not present");
            throw new DegreeNotFoundException();
        }

        final Degree degree = maybeDegree.get();
        final Map<String, Integer> progress = user == null ? null : user.getSubjectProgress();

        ModelAndView mav = new ModelAndView("degree/index");
        mav.addObject("degree", degree);
        mav.addObject("subjectProgress", progress);
        return mav;
    }

    @RequestMapping("/")
    public ModelAndView home() {
        return new ModelAndView("redirect:/degree/Ingenieria en Informatica");
    }
}
