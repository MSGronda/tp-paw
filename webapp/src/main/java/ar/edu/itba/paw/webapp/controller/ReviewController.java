package ar.edu.itba.paw.webapp.controller;
import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.DegreeService;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.services.SubjectService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.auth.UniAuthUser;
import ar.edu.itba.paw.webapp.exceptions.DegreeNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.webapp.form.ReviewForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Controller
public class ReviewController {

    private final UserService userService;
    private final SubjectService subjectService;
    private final ReviewService reviewService;

    private final DegreeService degreeService;

    @Autowired
    public ReviewController(UserService userService, SubjectService subjectService, ReviewService reviewService, DegreeService degreeService) {
        this.userService = userService;
        this.subjectService = subjectService;
        this.reviewService = reviewService;
        this.degreeService = degreeService;
    }

    @RequestMapping(value = "/review/{subjectId:\\d+\\.\\d+}", method = RequestMethod.POST)
    public ModelAndView review(@PathVariable final String subjectId, @Valid @ModelAttribute("ReviewForm") final ReviewForm reviewForm,
                               final BindingResult errors) throws SQLException {
        if(errors.hasErrors()){
            return reviewForm(subjectId, reviewForm);
        }
        Review review = reviewService.create(reviewForm.getAnonymous(),reviewForm.getEasy(), reviewForm.getTimeDemanding(), reviewForm.getText(), subjectId, loggedUser().getId());

        return new ModelAndView("redirect:/subject/" + subjectId);
    }
    @RequestMapping(value = "/review/{subjectId:\\d+\\.\\d+}", method = RequestMethod.GET)
    public ModelAndView reviewForm(@PathVariable final String subjectId, @ModelAttribute("ReviewForm") final ReviewForm reviewForm) {

        ModelAndView mav =  new ModelAndView("review/review");

        Optional<Subject> maybeSubject = subjectService.findById(subjectId);

        if( maybeSubject.isPresent()){
            Subject subject = maybeSubject.get();
            if(reviewService.didUserReviewDB(subjectId, loggedUser().getId())){
                return new ModelAndView("redirect:/subject/" + subjectId);
            }
            mav.addObject("subject", subject );
            return mav;
        }
        throw new SubjectNotFoundException();
    }

    @RequestMapping(value = "/review/{subjectId:\\d+\\.\\d+}/edit/{reviewId:\\d+}", method = RequestMethod.POST)
    public ModelAndView editReviewPost(@PathVariable final String subjectId, @PathVariable final Long reviewId,
                                       @ModelAttribute("ReviewForm") final ReviewForm reviewForm,
                                       final BindingResult errors){
        if(errors.hasErrors()){
            return editReview(subjectId, reviewId, reviewForm);
        }

        Optional<Review> maybeReview = reviewService.findById(reviewId);

        if(!maybeReview.isPresent()){
            throw new ReviewNotFoundException();
        }

        Review review = maybeReview.get();

        Integer easyBefore = review.getEasy();
        Integer timeDemandingBefore = review.getTimeDemanding();

        review.setText(reviewForm.getText());
        review.setEasy(reviewForm.getEasy());
        review.setTimeDemanding(reviewForm.getTimeDemanding());
        review.setAnonymous(reviewForm.getAnonymous());

        reviewService.update(review);
        reviewService.updateReviewStatistics(easyBefore, timeDemandingBefore, review);

        return new ModelAndView("redirect:/subject/" + subjectId);
    }

    @RequestMapping(value = "/review/{subjectId:\\d+\\.\\d+}/edit/{reviewId:\\d+}", method = RequestMethod.GET)
    public ModelAndView editReview(@PathVariable final String subjectId, @PathVariable final Long reviewId,
                                   @ModelAttribute("ReviewForm") final ReviewForm reviewForm){
        ModelAndView mav = new ModelAndView("review/edit");

        Optional<Subject> subject = subjectService.findById(subjectId);
        if(!subject.isPresent()){
            throw new SubjectNotFoundException();
        }

        Optional<Review> review = reviewService.findById(reviewId);

        if(!review.isPresent() || loggedUser().getId() != review.get().getUserId()){
            return new ModelAndView("redirect:/subject/" + subjectId);
        }

        mav.addObject("subject", subject.get());
        mav.addObject("review", review.get());
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

    @ModelAttribute("degrees")
    public List<Degree> degrees(){
        return degreeService.getAll();
    }

}
