package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.services.SubjectService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.ReviewForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.naming.Binding;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class HelloWorldController {
    private final UserService userService;
    private final SubjectService subjectService;

    private final ReviewService reviewService;

    @Autowired
    public HelloWorldController(UserService userService, SubjectService subjectService, ReviewService reviewService) {
        this.userService = userService;
        this.subjectService = subjectService;
        this.reviewService = reviewService;
    }

    @RequestMapping("/helloworld")
    public ModelAndView home() {
        return new ModelAndView("helloworld/index");
    }

    @RequestMapping("/subject")
    public ModelAndView subject_info() {
        ModelAndView mav = new ModelAndView("helloworld/subject_info");

        List<Review> reviews = new ArrayList<>();

        Review rev1 = new Review(1, 1, "1", true, true, "This subject is crap");
        Review rev2 = new Review(2, 2, "1", true, false,"This subject is okay");
        Review rev3 = new Review(3, 2, "1", false, true, "NIIIIIIIIIINOOOOOOOOOOOOOaaaaaaaaaaaaaaaaaaaa aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        reviews.add(rev1);
        reviews.add(rev2);
        reviews.add(rev3);

        mav.addObject("reviews", reviews);
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
        //TODO - chequear si existe el usuario
        final User user = userService.create(reviewForm.getEmail(), null, null);
        final Review review = reviewService.create(reviewForm.getEasy(), reviewForm.getTimeDemanding(), reviewForm.getText(), subjectId, user.getId() );
        return new ModelAndView("redirect:/");
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
