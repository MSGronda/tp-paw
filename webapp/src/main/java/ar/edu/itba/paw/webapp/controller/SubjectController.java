package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.auth.UniAuthUser;
import ar.edu.itba.paw.webapp.exceptions.DegreeNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.SubjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Long.parseLong;

@Controller
public class SubjectController {
    private final SubjectService subjectService;
    private final ProfessorService professorService;
    private final ReviewService reviewService;
    private final DegreeService degreeService;
    private final SubjectClassService subjectClassService;

    private final UserService userService;

    @Autowired
    public SubjectController(SubjectService subjectService, ReviewService reviewService,
                             ProfessorService professorService, SubjectClassService subjectClassService,
                             DegreeService degreeService, UserService userService ) {
        this.subjectClassService = subjectClassService;
        this.subjectService = subjectService;
        this.reviewService = reviewService;
        this.professorService = professorService;
        this.degreeService = degreeService;
        this.userService = userService;
    }

    @RequestMapping("/subject/{id:\\d+\\.\\d+}")
    public ModelAndView subjectInfo(@PathVariable String id) {
        final Optional<Subject> maybeSubject = subjectService.findById(id);
        if(!maybeSubject.isPresent()) {
            throw new SubjectNotFoundException("No subject with given id");
        }
        final Subject subject = maybeSubject.get();

        final ReviewStatistic stats = reviewService.getReviewStatBySubject(id).orElseGet(() -> new ReviewStatistic(id));

        final Optional<Degree> maybeDegree = degreeService.findById(1L);
        if(!maybeDegree.isPresent()) {
            throw new DegreeNotFoundException();
        }
        Degree degree = maybeDegree.get();
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

        final List<Review> reviews = reviewService.getAllSubjectReviewsWithUsername(id);

        final Map<String,String> prereqNames = subjectService.findPrerequisitesName(id);

        final List<SubjectClass> classes = subjectClassService.getBySubId(id);

        long userId;
        if(loggedUser() == null)
            userId = -1;
        else
            userId = loggedUser().getId();

        final Map<Long, Integer> userVotes = reviewService.userReviewVoteByIdSubAndIdUser(id, userId);

        ModelAndView mav = new ModelAndView("subjects/subject_info");
        mav.addObject("reviews", reviews);
        mav.addObject("professors", professors);
        mav.addObject("time", stats.getTimeDifficulty());
        mav.addObject("subject", subject);
        mav.addObject("year",year);
        mav.addObject("prereqNames", prereqNames.entrySet());
        mav.addObject("difficulty", stats.getDifficulty());
        mav.addObject("classes", classes);
        mav.addObject("userVotes", userVotes);
        return mav;
    }
    @ModelAttribute("loggedUser")
    public User loggedUser(){
        Object maybeUniAuthUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if( maybeUniAuthUser.toString().equals("anonymousUser")){
            return null;
        }
        final UniAuthUser userDetails = (UniAuthUser) maybeUniAuthUser ;
        return userService.getUserWithEmail(userDetails.getUsername()).orElse(null);
    }
}
