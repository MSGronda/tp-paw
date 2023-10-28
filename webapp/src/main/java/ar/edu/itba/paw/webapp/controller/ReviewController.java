package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewVote;
import ar.edu.itba.paw.models.exceptions.*;
import ar.edu.itba.paw.services.AuthUserService;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.services.SubjectService;
import ar.edu.itba.paw.webapp.dto.ReviewDto;
import ar.edu.itba.paw.webapp.dto.ReviewVoteDto;
import ar.edu.itba.paw.webapp.form.ReviewForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Path("reviews")
@Controller
public class ReviewController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    private final SubjectService subjectService;
    private final ReviewService reviewService;
    private final AuthUserService authUserService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public ReviewController(
            final SubjectService subjectService,
            final ReviewService reviewService,
            final AuthUserService authUserService
    ) {
        this.subjectService = subjectService;
        this.reviewService = reviewService;
        this.authUserService = authUserService;
    }

//    @RequestMapping(value = "/review/{subjectId:\\d+\\.\\d+}", method = RequestMethod.POST)
//    public ModelAndView review(
//            @PathVariable final String subjectId,
//            @Valid @ModelAttribute("ReviewForm") final ReviewForm reviewForm,
//            final BindingResult errors
//    ) {
//        if (errors.hasErrors()) {
//            LOGGER.debug("Review form has errors");
//            return reviewForm(subjectId, reviewForm);
//        }
//
//        reviewService.create(
//                subjectId,
//                Review.builder()
//                        .anonymous(reviewForm.getAnonymous())
//                        .difficulty(reviewForm.getDifficultyEnum())
//                        .timeDemanding(reviewForm.getTimeDemandingEnum())
//                        .text(reviewForm.getText())
//                        .user(authUserService.getCurrentUser())
//        );
//
//        return new ModelAndView("redirect:/subject/" + subjectId);
//    }
//
//    @RequestMapping(value = "/review/{subjectId:\\d+\\.\\d+}", method = RequestMethod.GET)
//    public ModelAndView reviewForm(
//            @PathVariable final String subjectId,
//            @ModelAttribute("ReviewForm") final ReviewForm reviewForm
//    ) {
//        final Subject subject = subjectService.findById(subjectId).orElseThrow(SubjectNotFoundException::new);
//
//        if (reviewService.didUserReview(subject, authUserService.getCurrentUser())) {
//            return new ModelAndView("redirect:/subject/" + subjectId);
//        }
//
//        final ModelAndView mav = new ModelAndView("review/review");
//        mav.addObject("subject", subject);
//        return mav;
//    }
//
//    @RequestMapping(value = "/many-reviews", method = RequestMethod.GET)
//    public ModelAndView manyReviews(
//            @RequestParam(name = "r") final String subjectIds,
//            @RequestParam(name = "total") final int total,
//            @RequestParam(name = "current") final int current,
//            @ModelAttribute("ReviewForm") final ReviewForm reviewForm
//    ) {
//        final Subject subject = reviewService.manyReviewsGetFirstSubject(subjectIds);
//
//        if (reviewService.didUserReview(subject, authUserService.getCurrentUser())) {
//            return new ModelAndView("redirect:" + reviewService.manyReviewsNextUrl(subjectIds, current, total));
//        }
//
//        final ModelAndView mav = new ModelAndView("review/many-reviews");
//        mav.addObject("subject", subject);
//        mav.addObject("current", current);
//        mav.addObject("total", total);
//        return mav;
//    }
//
//    @RequestMapping(value = "/many-reviews", method = RequestMethod.POST)
//    public ModelAndView manyReviewsForm(
//            @RequestParam(name = "r") final String subjectIds,
//            @RequestParam(name = "total") final int total,
//            @RequestParam(name = "current") final int current,
//            @Valid @ModelAttribute("ReviewForm") final ReviewForm reviewForm,
//            final BindingResult errors
//    ) {
//        if (errors.hasErrors()) {
//            LOGGER.debug("Review form has errors");
//            return manyReviews(subjectIds, total, current, reviewForm);
//        }
//
//        reviewService.manyReviewsSubmit(
//                subjectIds,
//                Review.builder()
//                        .user(authUserService.getCurrentUser())
//                        .anonymous(reviewForm.getAnonymous())
//                        .text(reviewForm.getText())
//                        .timeDemanding(reviewForm.getTimeDemandingEnum())
//                        .difficulty(reviewForm.getDifficultyEnum())
//        );
//
//        return new ModelAndView("redirect:" + reviewService.manyReviewsNextUrl(subjectIds, current, total));
//    }
//
//    @RequestMapping("review/{subjectId:\\d+\\.\\d+}/delete/{reviewId:\\d+}")
//    public ModelAndView deleteReview(
//            @PathVariable final String subjectId,
//            @PathVariable final long reviewId
//    ) throws UnauthorizedException, ReviewNotFoundException {
//        reviewService.delete(reviewId);
//        return new ModelAndView("redirect:/review/" + subjectId + "/deletion/true");
//    }
//
//    @RequestMapping(value = "/review/{subjectId:\\d+\\.\\d+}/edit/{reviewId:\\d+}", method = RequestMethod.POST)
//    public ModelAndView editReviewPost(
//            @PathVariable final String subjectId,
//            @PathVariable final long reviewId,
//            @ModelAttribute("ReviewForm") final ReviewForm reviewForm,
//            final BindingResult errors
//    ) throws UnauthorizedException, ReviewNotFoundException {
//        if (errors.hasErrors()) {
//            return editReview(subjectId, reviewId, reviewForm);
//        }
//
//        final Review review = reviewService.findById(reviewId).orElseThrow(ReviewNotFoundException::new);
//
//        reviewService.update(
//                Review.builderFrom(review)
//                    .text(reviewForm.getText())
//                    .difficulty(reviewForm.getDifficultyEnum())
//                    .timeDemanding(reviewForm.getTimeDemandingEnum())
//                    .anonymous(reviewForm.getAnonymous())
//        );
//
//        return new ModelAndView("redirect:/subject/" + subjectId);
//    }
//
//    @RequestMapping(value = "/review/{subjectId:\\d+\\.\\d+}/edit/{reviewId:\\d+}", method = RequestMethod.GET)
//    public ModelAndView editReview(
//            @PathVariable final String subjectId,
//            @PathVariable final long reviewId,
//            @ModelAttribute("ReviewForm") final ReviewForm reviewForm
//    ) {
//        final Review review = reviewService.findById(reviewId).orElseThrow(ReviewNotFoundException::new);
//
//        if (!reviewService.canUserEditReview(authUserService.getCurrentUser(), review)) {
//            throw new UnauthorizedException();
//        }
//
//        final ModelAndView mav = new ModelAndView("review/edit");
//        mav.addObject("review", review);
//        return mav;
//    }
//
//    @RequestMapping(value = "/voteReview", method = RequestMethod.POST)
//    @ResponseBody
//    public String voteReview(@Valid @ModelAttribute("ReviewVoteForm") final ReviewVoteForm vote) {
//        reviewService.voteReview(vote.getReviewId(), vote.getVote());
//        return "voted";
//    }
//
//    @RequestMapping("review/{subjectId:\\d+\\.\\d+}/deletion/{successful:true|false}")
//    public ModelAndView deletionSuccess(@PathVariable final String subjectId, @PathVariable final String successful) {
//        final ModelAndView mav = new ModelAndView("review/delete_confirmation");
//        mav.addObject("subjectId", subjectId);
//        mav.addObject("successful", successful);
//        return mav;
//    }
    @GET
    @Produces("application/vnd.review-list.v1+json")
    public Response getReviews(
            @QueryParam("page") @DefaultValue("1") final int page,
            @QueryParam("orderBy") @DefaultValue("difficulty") final String orderBy,
            @QueryParam("dir") @DefaultValue("desc") final String dir,
            @QueryParam("userId") final Long userId,
            @QueryParam("subjectId") final String subjectId
    ){
        final List<Review> reviews = reviewService.get(userId, subjectId, page, orderBy, dir);

        if(reviews.isEmpty())
            return Response.noContent().build();

        final List<ReviewDto> reviewDtos = reviews.stream().map(review -> ReviewDto.fromReview(uriInfo, review)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<ReviewDto>>(reviewDtos){}).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createReview(@Valid @ModelAttribute("reviewForm") final ReviewForm reviewForm){

        final Review newReview = reviewService.create(
            reviewForm.getSubjectId(),
            Review.builder()
            .anonymous(reviewForm.getAnonymous())
            .difficulty(reviewForm.getDifficultyEnum())
            .timeDemanding(reviewForm.getTimeDemandingEnum())
            .text(reviewForm.getText())
            .user(authUserService.getCurrentUser())
        );

        final URI reviewUri = uriInfo.getBaseUriBuilder().path("/reviews/").path(String.valueOf(newReview.getId())).build();

        return Response.created(reviewUri).build();
    }

    @GET
    @Path("/{id}")
    public Response getReviewById(
            @PathParam("id") final Long reviewId
    ){
        final Review review = reviewService.findById(reviewId).orElseThrow(ReviewNotFoundException::new);
        return Response.ok(ReviewDto.fromReview(uriInfo, review)).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateReview(
            @PathParam("id") final Long reviewId,
            @Valid @ModelAttribute("reviewForm") final ReviewForm reviewForm
    ){
        // TODO check: si el review no existe, tira: "405 HTTP method PUT is not supported by this URL".
        // No se por que.

        // TODO: esto pero en un paso (?)
        final Review review = reviewService.findById(reviewId).orElseThrow(ReviewNotFoundException::new);
        reviewService.update(
            Review.builderFrom(review)
                    .text(reviewForm.getText())
                    .difficulty(reviewForm.getDifficultyEnum())
                    .timeDemanding(reviewForm.getTimeDemandingEnum())
                    .anonymous(reviewForm.getAnonymous())
        );

        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteReview(
            @PathParam("id") final Long reviewId
    ){
        reviewService.delete(reviewId);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/votes")
    public Response getReviewVotes(
            @PathParam("id") final Long reviewId,
            @QueryParam("userId") final Long userId
    ){
        final List<ReviewVote> votes = reviewService.getVotes(reviewId, userId);

        if(votes.isEmpty())
            return Response.noContent().build();

        final List<ReviewVoteDto> voteDtos = votes.stream().map(vote -> ReviewVoteDto.fromReviewVote(uriInfo, vote)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<ReviewVoteDto>>(voteDtos){}).build();
    }
}
