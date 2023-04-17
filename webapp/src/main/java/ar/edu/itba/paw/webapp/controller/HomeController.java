package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.services.DegreeService;
import ar.edu.itba.paw.services.ProfessorService;
import ar.edu.itba.paw.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    private final DegreeService ds;
    private final SubjectService ss;

    private final ProfessorService ps;

    @Autowired
    public HomeController(DegreeService ds, SubjectService ss, ProfessorService ps) {
        this.ds = ds;
        this.ss = ss;
        this.ps = ps;
    }

    @RequestMapping("/")
    public ModelAndView home() {
        final List<Degree> degrees = ds.getAll();
        final Map<Long, Map<Integer,List<Subject>>> subsByDegSem = ss.getAllGroupedByDegIdAndSemester();
        final Map<String, List<Professor>> profsBySubId = ps.getAllGroupedBySubjectId();

        ModelAndView mav = new ModelAndView("home/index");
        mav.addObject("degrees", degrees);
        mav.addObject("subsByDegSem", subsByDegSem);
        mav.addObject("profsBySubId", profsBySubId);

        return mav;
    }
}
