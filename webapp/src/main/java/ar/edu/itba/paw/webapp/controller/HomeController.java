package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.services.DegreeService;
import ar.edu.itba.paw.services.ProfessorService;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.services.SubjectService;
import ar.edu.itba.paw.webapp.exceptions.DegreeNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.SubjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
public class HomeController {
    private final DegreeService ds;
    private final SubjectService ss;

    private final ProfessorService ps;

    private final ReviewService rs;

    @Autowired
    public HomeController(DegreeService ds, SubjectService ss, ProfessorService ps, ReviewService rs) {
        this.ds = ds;
        this.ss = ss;
        this.ps = ps;
        this.rs = rs;
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

        Map<String, Integer> reviewCount = new HashMap<>();
        Map<String, List<String>> prereqs = new HashMap<>();

        for( int year : years ){
            for( Subject subject : infSubsByYear.get(year)){
                reviewCount.put(subject.getId(), rs.getAllBySubject(subject.getId()).size());
                for( String id : subject.getPrerequisites()){
                    prereqs.putIfAbsent(subject.getId(), new ArrayList<>());
                    if(!ss.findById(id).isPresent()){
                        throw new SubjectNotFoundException();
                    }
                    prereqs.get(subject.getId()).add(ss.findById(id).get().getName());
                }
            }
        }
        System.out.println(reviewCount);
        System.out.println(prereqs);
        System.out.println(prereqs.get("93.59"));

        ModelAndView mav = new ModelAndView("home/index");
        mav.addObject("degrees", degrees);
        mav.addObject("years", years);
        mav.addObject("infSubsByYear", infSubsByYear);
        mav.addObject("subjectReviewCount", reviewCount);
        mav.addObject("prereqNames", prereqs);
        mav.addObject("profsBySubId", profsBySubId);

        return mav;
    }
}
