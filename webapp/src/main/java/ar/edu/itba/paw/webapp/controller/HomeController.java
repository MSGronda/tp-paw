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

import javax.swing.text.html.Option;
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
        Optional<Degree> maybeDegree = degrees.stream().findFirst();

        if( !maybeDegree.isPresent() ){
            //TODO - error
            throw new DegreeNotFoundException();
        }

        final Map<Integer, List<Subject>> infSubsByYear = ss.getInfSubsByYear(maybeDegree.get().getId());
        final List<Subject> infElectives = ss.getInfElectives(maybeDegree.get().getId());

        Set<Integer> years = infSubsByYear.keySet();

        List<Map<String, Integer>> cardData = ss.getCardData(years, infSubsByYear, rs);
        List<Map<String, Integer>> electiveCardData = ss.getElectiveCardData(infElectives, rs);


        ModelAndView mav = new ModelAndView("home/index");
        mav.addObject("degrees", degrees);
        mav.addObject("years", years);
        mav.addObject("infSubsByYear", infSubsByYear);
        mav.addObject("electives", infElectives);
        mav.addObject("subjectReviewCount", cardData.get(0));
        mav.addObject("subjectDifficulty", cardData.get(1));
        mav.addObject("subjectTime", cardData.get(2));
        mav.addObject("electiveReviewCount", electiveCardData.get(0));
        mav.addObject("electiveDifficulty", electiveCardData.get(1));
        mav.addObject("electiveTime", electiveCardData.get(2));

        return mav;
    }
}
