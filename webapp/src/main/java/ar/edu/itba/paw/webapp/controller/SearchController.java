package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class SearchController {
    private final SubjectService subjectService;

    @Autowired
    public SearchController(SubjectService subjectService) {
        this.subjectService = subjectService;
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

        ModelAndView mav = new ModelAndView("helloworld/search");
        mav.addObject("subjects", subjects);
        mav.addObject("query", name);

        return mav;
    }
}
