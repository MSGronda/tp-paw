package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.ProfessorService;
import ar.edu.itba.paw.services.SubjectService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.ReviewForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.*;

@Controller
public class HelloWorldController {
    private final UserService userService;
    private final SubjectService subjectService;
    private final ProfessorService professorService;
    private final ReviewService reviewService;




    @Autowired
    public HelloWorldController(UserService userService, SubjectService subjectService, ReviewService reviewService, ProfessorService professorService) {
        this.userService = userService;
        this.subjectService = subjectService;
        this.reviewService = reviewService;
        this.professorService = professorService;
    }

    @RequestMapping("/helloworld")
    public ModelAndView home() {
        return new ModelAndView("helloworld/index");
    }

    @RequestMapping("/subject/{id:\\d+\\.\\d+}")
    public ModelAndView subject_info(@PathVariable String id) {
        final Optional<Subject> maybeSubject = subjectService.findById(id);
        if(!maybeSubject.isPresent()) {
            throw new SubjectNotFoundException("No subject with given id");
        }
        final Subject subject = maybeSubject.get();

        final List<Professor> professors = professorService.getAllBySubject(id);

        final List<Review> reviews = reviewService.getAllBySubject(id);

        final Map<String,String> prereqNames = subjectService.findPrerequisitesName(id);

        ModelAndView mav = new ModelAndView("helloworld/subject_info");
        mav.addObject("reviews", reviews);
        mav.addObject("professors", professors);
        mav.addObject("subject", subject);
        mav.addObject("prereqNames", prereqNames.entrySet());
        return mav;
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

        final Map<Subject, List<Professor>> profs = new HashMap<>();

        for(Subject sub : subjects) {
            profs.put(sub, professorService.getAllBySubject(sub.getId()));
        }


        ModelAndView mav = new ModelAndView("helloworld/search");
        mav.addObject("subjects", subjects);
        mav.addObject("query", name);
        mav.addObject("profs", profs);

        return mav;
    }

    @RequestMapping("/profile/{id:\\d+}")
    public ModelAndView profile(@PathVariable long id) {
        final Optional<User> maybeUser = userService.findById(id);
        if(!maybeUser.isPresent()) {
            throw new UserNotFoundException();
        }

        final User user = maybeUser.get();
        ModelAndView mav = new ModelAndView("helloworld/profile");
        mav.addObject("user", user);

        return mav;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(
        @RequestParam String username,
        @RequestParam String email,
        @RequestParam String password
    ) throws SQLException {
        final User newUser = userService.create(email, password, username);

        return new ModelAndView("redirect:/profile/" + newUser.getId());
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView registerForm() {
        return new ModelAndView("helloworld/register");
    }

    @RequestMapping(value = "/review/{subjectId:\\d+\\.\\d+}", method = RequestMethod.POST)
    public ModelAndView review(@PathVariable final String subjectId, @Valid @ModelAttribute("ReviewForm") final ReviewForm reviewForm,
                               final BindingResult errors) throws SQLException {
        if(errors.hasErrors()){
            return reviewForm(subjectId, reviewForm);
        }

        Optional<User> maybeUser = userService.getUserWithEmail(reviewForm.getEmail());
        if(!maybeUser.isPresent() ){
            final User user = userService.create(reviewForm.getEmail(), null, null);
            final Review review = reviewService.create(reviewForm.getEasy(), reviewForm.getTimeDemanding(), reviewForm.getText(), subjectId, user.getId(), reviewForm.getEmail());
        }
        else{
            final Review review = reviewService.create(reviewForm.getEasy(), reviewForm.getTimeDemanding(), reviewForm.getText(), subjectId, maybeUser.get().getId(), reviewForm.getEmail());
        }

        return new ModelAndView("redirect:/subject/" + subjectId);
    }
    @RequestMapping(value = "/review/{subjectId:\\d+\\.\\d+}", method = RequestMethod.GET)
    public ModelAndView reviewForm(@PathVariable final String subjectId, @ModelAttribute("ReviewForm") final ReviewForm reviewForm) {
        ModelAndView mav =  new ModelAndView("helloworld/review");

        Optional<Subject> maybeSubject = subjectService.findById(subjectId);

        if( maybeSubject.isPresent()){
            Subject subject = maybeSubject.get();
            mav.addObject("subject", subject );
            return mav;
        }
        throw new RuntimeException();
    }


}
