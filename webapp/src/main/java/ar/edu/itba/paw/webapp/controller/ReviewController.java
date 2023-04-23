package ar.edu.itba.paw.webapp.controller;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.services.SubjectService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.form.ReviewForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.Optional;

@Controller
public class ReviewController {

    private final UserService userService;
    private final SubjectService subjectService;
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(UserService userService, SubjectService subjectService, ReviewService reviewService) {
        this.userService = userService;
        this.subjectService = subjectService;
        this.reviewService = reviewService;
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
            final Review review = reviewService.create(reviewForm.getAnonymous(),reviewForm.getEasy(), reviewForm.getTimeDemanding(), reviewForm.getText(), subjectId, user.getId(), reviewForm.getEmail());
        }
        else{
            final Review review = reviewService.create(reviewForm.getAnonymous(),reviewForm.getEasy(), reviewForm.getTimeDemanding(), reviewForm.getText(), subjectId, maybeUser.get().getId(), reviewForm.getEmail());
        }

        return new ModelAndView("redirect:/subject/" + subjectId);
    }
    @RequestMapping(value = "/review/{subjectId:\\d+\\.\\d+}", method = RequestMethod.GET)
    public ModelAndView reviewForm(@PathVariable final String subjectId, @ModelAttribute("ReviewForm") final ReviewForm reviewForm) {
        ModelAndView mav =  new ModelAndView("review/review");

        Optional<Subject> maybeSubject = subjectService.findById(subjectId);

        if( maybeSubject.isPresent()){
            Subject subject = maybeSubject.get();
            mav.addObject("subject", subject );
            return mav;
        }
        throw new RuntimeException();
    }

}
