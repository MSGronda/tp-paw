package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.exceptions.DegreeNotFoundException;
import ar.edu.itba.paw.services.AuthUserService;
import ar.edu.itba.paw.services.DegreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DegreeController {
    private final DegreeService degreeService;
    private final AuthUserService authUserService;

    @Autowired
    public DegreeController(
            final DegreeService degreeService,
            final AuthUserService authUserService
    ) {
        this.degreeService = degreeService;
        this.authUserService = authUserService;
    }

//    @RequestMapping("/degrees")
//    public ModelAndView degrees() {
//        final ModelAndView mav = new ModelAndView("degree/degrees");
//        mav.addObject("degrees", degreeService.getAll());
//        return mav;
//    }
//
//    @RequestMapping("/degree")
//    public ModelAndView degree() {
//        return new ModelAndView("redirect:/degree/" + authUserService.getCurrentUser().getDegree().getId());
//    }
//
//    @RequestMapping("/degree/{id:\\d+}")
//    public ModelAndView degree(@PathVariable final long id) {
//        final Degree degree = degreeService.findById(id).orElseThrow(DegreeNotFoundException::new);
//
//        final ModelAndView mav = new ModelAndView("degree/index");
//        mav.addObject("degree", degree);
//        mav.addObject("subjectProgress", authUserService.getCurrentUser().getAllSubjectProgress());
//        mav.addObject("relevantFilters", degreeService.getRelevantFiltersForDegree(degree));
//        return mav;
//    }
}
