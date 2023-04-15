package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.services.ProfessorService;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.services.SubjectService;
import ar.edu.itba.paw.webapp.exceptions.SubjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class SubjectController {
    private final SubjectService subjectService;
    private final ProfessorService professorService;
    private final ReviewService reviewService;

    @Autowired
    public SubjectController(SubjectService subjectService, ReviewService reviewService, ProfessorService professorService) {
        this.subjectService = subjectService;
        this.reviewService = reviewService;
        this.professorService = professorService;
    }

    @RequestMapping("/subject/{id:\\d+\\.\\d+}")
    public ModelAndView subject_info(@PathVariable String id) {
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

        final List<Professor> professors = professorService.getAllBySubject(id);

        final List<Review> reviews = reviewService.getAllBySubject(id);

        final Map<String,String> prereqNames = subjectService.findPrerequisitesName(id);

        ModelAndView mav = new ModelAndView("helloworld/subject_info");
        mav.addObject("reviews", reviews);
        mav.addObject("professors", professors);
        mav.addObject("subject", subject);
        mav.addObject("prereqNames", prereqNames.entrySet());
        mav.addObject("difficulty", difficulty);
        return mav;
    }

}
