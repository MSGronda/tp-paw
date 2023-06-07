package ar.edu.itba.paw.webapp.controller;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.services.exceptions.NoGrantedPermissionException;
import ar.edu.itba.paw.webapp.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.webapp.form.ReviewForm;
import ar.edu.itba.paw.webapp.form.ReviewVoteForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@Controller
public class ReviewController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    private final SubjectService subjectService;
    private final ReviewService reviewService;
    private final AuthUserService authUserService;

    @Autowired
    public ReviewController(SubjectService subjectService, ReviewService reviewService, AuthUserService authUserService) {
        this.subjectService = subjectService;
        this.reviewService = reviewService;
        this.authUserService = authUserService;
    }

    @RequestMapping(value = "/review/{subjectId:\\d+\\.\\d+}", method = RequestMethod.POST)
    public ModelAndView review(
            @PathVariable final String subjectId,
            @Valid @ModelAttribute("ReviewForm") final ReviewForm reviewForm,
            final BindingResult errors
    ) throws SQLException {
        if(errors.hasErrors()){
            LOGGER.warn("Review form has errors");
            return reviewForm(subjectId, reviewForm);
        }

        final Subject subject = subjectService.findById(subjectId).orElseThrow(SubjectNotFoundException::new);

        reviewService.create(
            Review.builder()
                .anonymous(reviewForm.getAnonymous())
                .difficulty(reviewForm.getDifficultyEnum())
                .timeDemanding(reviewForm.getTimeDemandingEnum())
                .text(reviewForm.getText())
                .subject(subject)
                .user(authUserService.getCurrentUser())
                .build()
        );

        return new ModelAndView("redirect:/subject/" + subjectId);
    }
    @RequestMapping(value = "/review/{subjectId:\\d+\\.\\d+}", method = RequestMethod.GET)
    public ModelAndView reviewForm(
            @PathVariable final String subjectId,
            @ModelAttribute("ReviewForm") final ReviewForm reviewForm
    ) {

        ModelAndView mav =  new ModelAndView("review/review");

        Optional<Subject> maybeSubject = subjectService.findById(subjectId);

        if( maybeSubject.isPresent()){
            Subject subject = maybeSubject.get();
            if(reviewService.didUserReview(subject, authUserService.getCurrentUser())){
                return new ModelAndView("redirect:/subject/" + subjectId);
            }
            mav.addObject("subject", subject );
            return mav;
        }
        LOGGER.warn("No subject for id {}",subjectId);
        throw new SubjectNotFoundException();
    }

    @RequestMapping(value = "/many-reviews", method = RequestMethod.GET)
    public ModelAndView manyReviews(
            @RequestParam Map<String, String> params,
            @ModelAttribute("ReviewForm") final ReviewForm reviewForm
    ){
        if(!params.containsKey("r") || !params.containsKey("total") || !params.containsKey("current")){
            return new ModelAndView("redirect:/");
        }

        final String subjectId = reviewService.getFirstSubjectIdFromReviewList(params.get("r"));
        final Optional<Subject> maybeSubject = subjectService.findById(subjectId);

        if( !maybeSubject.isPresent()){
            LOGGER.warn("No subject for id {}",subjectId);
            throw new SubjectNotFoundException();
        }
        final Subject subject = maybeSubject.get();

        if(reviewService.didUserReview(subject, authUserService.getCurrentUser())){
            return new ModelAndView("redirect:/");
        }

        ModelAndView mav =  new ModelAndView("review/many-reviews");
        mav.addObject("subject", subject);
        mav.addObject("current", Integer.parseInt(params.get("current")));
        mav.addObject("total", Integer.parseInt(params.get("total")));
        mav.addObject("paramString", "?r=" +params.get("r") + "&current=" +
                params.get("current") + "&total=" + params.get("total"));

        return mav;
    }

    @RequestMapping(value = "/many-reviews", method = RequestMethod.POST)
    public ModelAndView manyReviewsForm(
            @RequestParam Map<String, String> params,
            @Valid @ModelAttribute("ReviewForm") final ReviewForm reviewForm,
            final BindingResult errors
    ) throws SQLException {
        if(!params.containsKey("r") || !params.containsKey("total") || !params.containsKey("current")){
            return new ModelAndView("redirect:/");
        }
        if(errors.hasErrors()){
            LOGGER.warn("Review form has errors");
            return manyReviews(params, reviewForm);
        }

        final String subjectId = reviewService.getFirstSubjectIdFromReviewList(params.get("r"));
        final Subject subject = subjectService.findById(subjectId).orElseThrow(SubjectNotFoundException::new);
        params.put("r", reviewService.removeFirstSubjectIdFromReviewList(params.get("r")));
        params.put("current", Integer.toString(Integer.parseInt(params.get("current") + 1 )));

        if(!reviewService.didUserReview(subject, authUserService.getCurrentUser())){
            reviewService.create(
                    Review.builder()
                            .anonymous(reviewForm.getAnonymous())
                            .difficulty(reviewForm.getDifficultyEnum())
                            .timeDemanding(reviewForm.getTimeDemandingEnum())
                            .text(reviewForm.getText())
                            .subject(subject)
                            .user(authUserService.getCurrentUser())
                            .build()
            );
        }

        if(!reviewService.canContinueReviewing(params.get("r"))){
            return new ModelAndView("redirect:/");
        }
        return new ModelAndView("redirect:/many-reviews?r="+params.get("r") + "&current=" + params.get("current")+"&total="+  params.get("total"));
    }

    @RequestMapping("review/{subjectId:\\d+\\.\\d+}/delete/{reviewId:\\d+}")
    public ModelAndView deleteReview(
            @PathVariable final String subjectId,
            @PathVariable final Long reviewId
    ) {
        Optional<Review> maybeReview = reviewService.findById(reviewId);
        if(!maybeReview.isPresent()){

            throw new ReviewNotFoundException();
        }
        Review review = maybeReview.get();

        try {
            reviewService.delete(review);
        } catch (NoGrantedPermissionException e) {
            return new ModelAndView("redirect:/review/" + subjectId + "/deletion/false");
        }

        return new ModelAndView("redirect:/review/" + subjectId + "/deletion/true");
    }

    @RequestMapping(value = "/review/{subjectId:\\d+\\.\\d+}/edit/{reviewId:\\d+}", method = RequestMethod.POST)
    public ModelAndView editReviewPost(
            @PathVariable final String subjectId,
            @PathVariable final Long reviewId,
            @ModelAttribute("ReviewForm") final ReviewForm reviewForm,
            final BindingResult errors
    ){
        if(errors.hasErrors()){
            return editReview(subjectId, reviewId, reviewForm);
        }

        Optional<Review> maybeReview = reviewService.findById(reviewId);

        if(!maybeReview.isPresent()){
            throw new ReviewNotFoundException();
        }

        Review review = maybeReview.get();

        Review updatedRev = Review.builderFrom(review)
            .text(reviewForm.getText())
            .difficulty(reviewForm.getDifficultyEnum())
            .timeDemanding(reviewForm.getTimeDemandingEnum())
            .anonymous(reviewForm.getAnonymous())
            .build();

        try {
            reviewService.update(updatedRev);
        } catch(NoGrantedPermissionException e) {
            return new ModelAndView("redirect:/error/unauthorized");
        }

        return new ModelAndView("redirect:/subject/" + subjectId);
    }

    @RequestMapping(value = "/review/{subjectId:\\d+\\.\\d+}/edit/{reviewId:\\d+}", method = RequestMethod.GET)
    public ModelAndView editReview(
            @PathVariable final String subjectId,
            @PathVariable final Long reviewId,
            @ModelAttribute("ReviewForm") final ReviewForm reviewForm
    ){
        ModelAndView mav = new ModelAndView("review/edit");

        Optional<Subject> subject = subjectService.findById(subjectId);
        if(!subject.isPresent()){
            throw new SubjectNotFoundException();
        }

        Optional<Review> review = reviewService.findById(reviewId);

        if(!review.isPresent() || !authUserService.getCurrentUser().equals(review.get().getUser())){
            return new ModelAndView("redirect:/subject/" + subjectId);
        }

        mav.addObject("subject", subject.get());
        mav.addObject("review", review.get());
        return mav;
    }

    @RequestMapping(value = "/voteReview", method = RequestMethod.POST)
    @ResponseBody
    public String voteReview(@Valid @ModelAttribute("ReviewVoteForm") final ReviewVoteForm vote) {
        if( !authUserService.isAuthenticated()){
            return "invalid parameters"; // we do not give any information on the inner workings
        }

        final User user = authUserService.getCurrentUser();
        final Review review = reviewService.findById(vote.getReviewId()).orElseThrow(ReviewNotFoundException::new);

        reviewService.voteReview(user, review, vote.getVote());

        return "voted";
    }

    @RequestMapping("review/{subjectId:\\d+\\.\\d+}/deletion/{successful:(?:true|false)}")
    public ModelAndView deletionSuccess(@PathVariable final String subjectId, @PathVariable final String successful){
        ModelAndView mav = new ModelAndView("review/delete_confirmation");

        mav.addObject("subjectId", subjectId);
        mav.addObject("successful", successful );

        return mav;
    }
}
