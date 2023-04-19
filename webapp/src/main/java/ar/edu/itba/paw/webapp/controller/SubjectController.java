package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.SubjectClass;
import ar.edu.itba.paw.services.ProfessorService;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.services.SubjectClassService;
import ar.edu.itba.paw.services.SubjectService;
import ar.edu.itba.paw.webapp.exceptions.SubjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class SubjectController {
    private final SubjectService subjectService;
    private final ProfessorService professorService;
    private final ReviewService reviewService;
    private final SubjectClassService subjectClassService;

    @Autowired
    public SubjectController(SubjectService subjectService, ReviewService reviewService,
                             ProfessorService professorService, SubjectClassService subjectClassService) {
        this.subjectClassService = subjectClassService;
        this.subjectService = subjectService;
        this.reviewService = reviewService;
        this.professorService = professorService;
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

        final List<Professor> professors = professorService.getAllBySubject(id);

        final List<Review> reviews = reviewService.getAllBySubject(id);

        final Map<String,String> prereqNames = subjectService.findPrerequisitesName(id);

        final List<SubjectClass> classes = subjectClassService.getBySubId(id);
        final Map<String,List<Professor>> classProfs = new HashMap<>();


        // TODO, redo this
        for(SubjectClass subjectClass: classes){
            classProfs.put(subjectClass.getIdClass(), professors.stream().filter(
                    professor -> subjectClass.getProfessorIds().contains(professor.getId())
            ).collect(Collectors.toList()));
        }


        ModelAndView mav = new ModelAndView("subjects/subject_info");
        mav.addObject("reviews", reviews);
        mav.addObject("professors", professors);
        mav.addObject("time", time);
        mav.addObject("subject", subject);
        mav.addObject("prereqNames", prereqNames.entrySet());
        mav.addObject("difficulty", difficulty);
        mav.addObject("classes", classes);
        mav.addObject("classProfs", classProfs);
        return mav;
    }

    @RequestMapping("/subject/**")
    public void unknownSubject() {
        throw new SubjectNotFoundException();
    }
}
