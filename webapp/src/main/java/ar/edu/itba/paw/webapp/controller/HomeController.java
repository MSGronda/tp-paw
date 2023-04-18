package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.services.DegreeService;
import ar.edu.itba.paw.services.ProfessorService;
import ar.edu.itba.paw.services.SubjectService;
import ar.edu.itba.paw.webapp.exceptions.DegreeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        if( !degrees.stream().findFirst().isPresent() ){
            //TODO - error
            throw new DegreeNotFoundException();
        }
        final Map<Integer, List<Subject>> infSubsBySem = subsByDegSem.get(degrees.stream().findFirst().get().getId());

        Map<Integer, List<Subject>> infSubsByYear = new HashMap<>();


        for( int semester : infSubsBySem.keySet()){
            if(semester % 2 == 0){
                List<Subject> aux = infSubsBySem.get(semester-1);
                aux.addAll(infSubsBySem.get(semester));
                infSubsByYear.put( semester/2, aux);
            }
        }

        Set<Integer> years = infSubsByYear.keySet();



        ModelAndView mav = new ModelAndView("home/index");
        mav.addObject("years", years);
        mav.addObject("infSubsByYear", infSubsByYear);
        mav.addObject("profsBySubId", profsBySubId);

        return mav;
    }
}
