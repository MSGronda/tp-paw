package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.DegreeService;
import ar.edu.itba.paw.services.ProfessorService;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.services.SubjectService;
import ar.edu.itba.paw.webapp.exceptions.DegreeNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.SubjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.Long.parseLong;

@Controller
public class SubjectController {
    private final SubjectService subjectService;
    private final ProfessorService professorService;
    private final ReviewService reviewService;
    private final DegreeService degreeService;

    @Autowired
    public SubjectController(SubjectService subjectService, ReviewService reviewService, ProfessorService professorService, DegreeService degreeService) {
        this.subjectService = subjectService;
        this.reviewService = reviewService;
        this.professorService = professorService;
        this.degreeService = degreeService;
    }

    @RequestMapping("/subject/{id:\\d+\\.\\d+}")
    public ModelAndView subjectInfo(@PathVariable String id) {
        final Optional<Subject> maybeSubject = subjectService.findById(id);
        if(!maybeSubject.isPresent()) {
            throw new SubjectNotFoundException("No subject with given id");
        }
        final Subject subject = maybeSubject.get();

        final Optional<Integer> maybeDifficulty = reviewService.getDifficultyBySubject(id);
        final Integer difficulty;
        if(!maybeDifficulty.isPresent()){
            difficulty = -1;
        } else difficulty = maybeDifficulty.get();

        final Optional<Integer> maybeTime = reviewService.getTimeBySubject(id);
        final Integer time;
        if(!maybeTime.isPresent()) {
            time = -1;
        } else time = maybeTime.get();

        final Optional<Degree> maybealgo = degreeService.findById(1L);
        if(!maybealgo.isPresent()) {
            throw new DegreeNotFoundException();
        }
        Degree degree = maybealgo.get();
        double maxYear = 0;
        for(Semester semester : degree.getSemesters()) {
            List<String> subjects = semester.getSubjectIds();
            if(subjects.contains(id)){
                maxYear = Math.ceil(semester.getNumber()/2.0);
                break;
            }
        }
        int year = (int) maxYear;
        final List<Professor> professors = professorService.getAllBySubject(id);

        final List<Review> reviews = reviewService.getAllBySubject(id);

        final Map<String,String> prereqNames = subjectService.findPrerequisitesName(id);

        ModelAndView mav = new ModelAndView("subjects/subject_info");
        mav.addObject("reviews", reviews);
        mav.addObject("professors", professors);
        mav.addObject("time", time);
        mav.addObject("subject", subject);
        mav.addObject("year",year);
        mav.addObject("prereqNames", prereqNames.entrySet());
        mav.addObject("difficulty", difficulty);
        return mav;
    }
}
