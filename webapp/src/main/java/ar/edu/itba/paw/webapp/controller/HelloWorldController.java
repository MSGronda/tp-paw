package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.DegreeService;
import ar.edu.itba.paw.services.ProfessorService;
import ar.edu.itba.paw.services.SubjectService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.exceptions.DegreeNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class HelloWorldController {
    private final UserService userService;
    private final ProfessorService professorService;
    private final SubjectService subjectService;
    private final DegreeService degreeService;

    @Autowired
    public HelloWorldController(UserService userService, SubjectService subjectService, DegreeService degreeService, ProfessorService professorService) {
        this.userService = userService;
        this.subjectService = subjectService;
        this.degreeService = degreeService;
        this.professorService = professorService;
    }

    @RequestMapping("/helloworld")
    public ModelAndView home() {
        return new ModelAndView("helloworld/index");
    }

    @RequestMapping("/subject/{degree_id:\\d+}/{id:\\d+\\.\\d+}")
    public ModelAndView subject_info(@PathVariable Long degree_id,@PathVariable String id) {
        final Optional<Subject> maybeSubject = subjectService.findById(id);
        if(!maybeSubject.isPresent()) {
            throw new SubjectNotFoundException("No subject with given id");
        }
        final Subject subject = maybeSubject.get();
        final Optional<Degree> maybeDegree = degreeService.findById(degree_id);
        if(!maybeDegree.isPresent()) {
            throw new DegreeNotFoundException("No degree with given id");
        }
        final Degree degree = maybeDegree.get();
        List<Review> reviews = new ArrayList<>();

        Review rev1 = new Review(1, 1, 1, "Algebra Review", "This subject is crap");
        Review rev2 = new Review(2, 2, 1, "Algebra asdf", "This subject is okay");
        Review rev3 = new Review(3, 2, 1, "NOOOOOOOOOONIIIIIIIIIIIII", "NIIIIIIIIIINOOOOOOOOOOOOOaaaaaaaaaaaaaaaaaaaa aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        reviews.add(rev1);
        reviews.add(rev2);
        reviews.add(rev3);

        final List<Professor> professors = professorService.getAllBySubject(id);


        ModelAndView mav = new ModelAndView("helloworld/subject_info");
        mav.addObject("degree", degree);
        mav.addObject("reviews", reviews);
        mav.addObject("professors", professors);
        mav.addObject("subject", subject);
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

    @RequestMapping(value = "/review", method = RequestMethod.GET)
    public ModelAndView reviewForm() {
        return new ModelAndView("helloworld/review");
    }
}
