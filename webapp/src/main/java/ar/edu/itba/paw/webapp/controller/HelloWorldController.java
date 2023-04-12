package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.SubjectService;
import ar.edu.itba.paw.services.UserService;
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
    private final SubjectService subjectService;

    @Autowired
    public HelloWorldController(UserService userService, SubjectService subjectService) {
        this.userService = userService;
        this.subjectService = subjectService;
    }

    @RequestMapping("/")
    public ModelAndView home() {
        return new ModelAndView("helloworld/index");
    }

    @RequestMapping("/subject")
    public ModelAndView subject_info() {
        ModelAndView mav = new ModelAndView("helloworld/subject_info");

        List<Review> reviews = new ArrayList<>();

        Review rev1 = new Review(1, 1, 1, "Algebra Review", "This subject is crap");
        Review rev2 = new Review(2, 2, 1, "Algebra asdf", "This subject is okay");
        Review rev3 = new Review(3, 2, 1, "NOOOOOOOOOONIIIIIIIIIIIII", "NIIIIIIIIIINOOOOOOOOOOOOOaaaaaaaaaaaaaaaaaaaa aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        reviews.add(rev1);
        reviews.add(rev2);
        reviews.add(rev3);

        mav.addObject("reviews", reviews);
        return mav;
    }

    @RequestMapping("/search/{name}")
    public ModelAndView search(@PathVariable String name, @RequestParam(required = false) String ob) {
        final List<Subject> subjects;
        if(ob != null)
            subjects = subjectService.getByNameOrderBy(name, ob);
        else
            subjects = subjectService.getByName(name);



        ModelAndView mav = new ModelAndView("helloworld/search");
        mav.addObject("subjects", subjects);
        mav.addObject("query", name);

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
}
