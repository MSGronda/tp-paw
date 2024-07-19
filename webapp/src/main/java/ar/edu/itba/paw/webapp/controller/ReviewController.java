package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewVote;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.*;
import ar.edu.itba.paw.services.AuthUserService;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.services.SubjectService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.controller.utils.PaginationLinkBuilder;
import ar.edu.itba.paw.webapp.dto.ReviewDto;
import ar.edu.itba.paw.webapp.dto.ReviewVoteDto;
import ar.edu.itba.paw.webapp.form.ReviewForm;
import ar.edu.itba.paw.webapp.form.ReviewVoteForm;
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
    private final ReviewService reviewService;
    private final AuthUserService authUserService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public ReviewController(final ReviewService reviewService, final AuthUserService authUserService){
        this.reviewService = reviewService;
        this.authUserService = authUserService;
    }

    @GET
    @Produces("application/vnd.review-list.v1+json")
    public Response getReviews(
            @QueryParam("page") @DefaultValue("1") final int page,
            @QueryParam("orderBy") @DefaultValue("difficulty") final String orderBy,
            @QueryParam("dir") @DefaultValue("desc") final String dir,
            @QueryParam("userId") final Long userId,
            @QueryParam("subjectId") final String subjectId
    ){
        final User currentUser = authUserService.getCurrentUser();

        final List<Review> reviews = reviewService.get(currentUser, userId, subjectId, page, orderBy, dir);

        if(reviews.isEmpty()){
            return Response.noContent().build();
        }

        final List<ReviewDto> reviewDtos = reviews.stream().map(review -> ReviewDto.fromReview(uriInfo, currentUser, review)).collect(Collectors.toList());

        int lastPage = reviewService.getTotalPages(currentUser, userId, subjectId);

        Response.ResponseBuilder responseBuilder = Response.ok(new GenericEntity<List<ReviewDto>>(reviewDtos){});
        PaginationLinkBuilder.getResponsePaginationLinks(responseBuilder, uriInfo, page, lastPage);
        return responseBuilder.build();
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
        final User currentUser = authUserService.getCurrentUser();

        final Review review = reviewService.findById(reviewId).orElseThrow(ReviewNotFoundException::new);
        return Response.ok(ReviewDto.fromReview(uriInfo, currentUser, review)).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateReview(
            @PathParam("id") final Long reviewId,
            @Valid @ModelAttribute("reviewForm") final ReviewForm reviewForm
    ){
        final Review review = reviewService.findById(reviewId).orElseThrow(ReviewNotFoundException::new);
        final User currentUser = authUserService.getCurrentUser();

        reviewService.update(
            Review.builderFrom(review)
                    .text(reviewForm.getText())
                    .difficulty(reviewForm.getDifficultyEnum())
                    .timeDemanding(reviewForm.getTimeDemandingEnum())
                    .anonymous(reviewForm.getAnonymous()),
            currentUser
        );

        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteReview(
            @PathParam("id") final Long reviewId
    ){
        final Review review = reviewService.findById(reviewId).orElseThrow(ReviewNotFoundException::new);
        final User currentUser = authUserService.getCurrentUser();

        reviewService.delete(currentUser, review);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/votes")
    public Response getReviewVotes(
            @PathParam("id") final Long reviewId,
            @QueryParam("userId") final Long userId,
            @QueryParam("page") @DefaultValue("1") final int page
    ){
        final List<ReviewVote> votes = reviewService.getVotes(reviewId, userId, page);

        if(votes.isEmpty())
            return Response.noContent().build();

        final List<ReviewVoteDto> voteDtos = votes.stream().map(vote -> ReviewVoteDto.fromReviewVote(uriInfo, vote)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<ReviewVoteDto>>(voteDtos){}).build();
    }

    @POST
    @Path("/{id}/votes")
    public Response createReviewVote(
            @PathParam("id") final Long reviewId,
            @Valid @ModelAttribute("reviewVoteForm") final ReviewVoteForm reviewVoteForm
    ){
        final ReviewVote vote = reviewService.voteReview(reviewId, reviewVoteForm.getVoteType());
        return Response.ok(ReviewVoteDto.fromReviewVote(uriInfo, vote)).build();
    }

    @DELETE
    @Path("/{reviewId}/votes/{userId}")
    public Response deleteReviewVote(
            @PathParam("reviewId") final Long reviewId,
            @PathParam("userId") final Long userId
    ){
        final Review review = reviewService.findById(reviewId).orElseThrow(ReviewNotFoundException::new);
        final User user = authUserService.getCurrentUser();

        reviewService.deleteReviewVote(review, user);

        return Response.noContent().build();
    }

}
