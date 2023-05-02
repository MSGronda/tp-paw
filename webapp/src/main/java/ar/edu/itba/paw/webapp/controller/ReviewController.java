package ar.edu.itba.paw.webapp.controller;
import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.auth.UniAuthUser;
import ar.edu.itba.paw.webapp.exceptions.DegreeNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.webapp.form.ReviewForm;
import ar.edu.itba.paw.webapp.form.ReviewVoteForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

    private final AuthUserService authUserService;

    @Autowired
    public ReviewController(UserService userService, SubjectService subjectService, ReviewService reviewService, DegreeService degreeService, AuthUserService authUserService) {
        this.userService = userService;
        this.subjectService = subjectService;
        this.reviewService = reviewService;
        this.degreeService = degreeService;
        this.authUserService = authUserService;
    }

    @RequestMapping(value = "/review/{subjectId:\\d+\\.\\d+}", method = RequestMethod.POST)
    public ModelAndView review(@PathVariable final String subjectId, @Valid @ModelAttribute("ReviewForm") final ReviewForm reviewForm,
                               final BindingResult errors) throws SQLException {
        if(errors.hasErrors()){
            return reviewForm(subjectId, reviewForm);
        }
        Review review = reviewService.create(reviewForm.getAnonymous(),reviewForm.getEasy(), reviewForm.getTimeDemanding(), reviewForm.getText(), subjectId, authUserService.getCurrentUser().getId());

        return new ModelAndView("redirect:/subject/" + subjectId);
    }
    @RequestMapping(value = "/review/{subjectId:\\d+\\.\\d+}", method = RequestMethod.GET)
    public ModelAndView reviewForm(@PathVariable final String subjectId, @ModelAttribute("ReviewForm") final ReviewForm reviewForm) {

        ModelAndView mav =  new ModelAndView("review/review");

        Optional<Subject> maybeSubject = subjectService.findById(subjectId);

        if( maybeSubject.isPresent()){
            Subject subject = maybeSubject.get();
            if(reviewService.didUserReviewDB(subjectId, authUserService.getCurrentUser().getId())){
                return new ModelAndView("redirect:/subject/" + subjectId);
            }
            mav.addObject("subject", subject );
            return mav;
        }
        throw new SubjectNotFoundException();
    }

    @RequestMapping("review/{subjectId:\\d+\\.\\d+}/delete/{reviewId:\\d+}")
    public ModelAndView deleteReview(@PathVariable final String subjectId, @PathVariable final Long reviewId){

        Optional<Review> maybeReview = reviewService.findById(reviewId);
        if(!maybeReview.isPresent()){
            throw new ReviewNotFoundException();
        }

        Review review = maybeReview.get();

        if( authUserService.getCurrentUser().getId() != review.getUserId() ){
            return new ModelAndView("redirect:/subject/" + subjectId);
        }

        reviewService.deleteReviewVoteByReviewId(reviewId);
        reviewService.deleteReviewStatistics(review);
        reviewService.delete(review);

        return new ModelAndView("redirect:/subject/" + subjectId);
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

        if(!review.isPresent() || authUserService.getCurrentUser().getId() != review.get().getUserId()){
            return new ModelAndView("redirect:/subject/" + subjectId);
        }

        mav.addObject("subject", subject.get());
        mav.addObject("review", review.get());
        return mav;
    }

    @RequestMapping(value = "/voteReview", method = RequestMethod.POST)
    @ResponseBody
    public String voteReview(@Valid @ModelAttribute("ReviewVoteForm") final ReviewVoteForm vote
    ) {
        if( !authUserService.isAuthenticated()){
            return "invalid parameters"; // we do not give any information on the inner workings
        }
        int resp, voteValue = vote.getVote();
        User user = authUserService.getCurrentUser();
        if(voteValue != 0)
            resp = reviewService.voteReview(user.getId(), vote.getReviewId(),voteValue);
        else
            resp = reviewService.deleteReviewVote(user.getId(), vote.getReviewId());

        if(resp != 1){
            return "invalid parameters"; // we do not give any information on the inner workings
        }
        return "voted";
    }

    @ModelAttribute("degrees")
    public List<Degree> degrees(){
        return degreeService.getAll();
    }

}
