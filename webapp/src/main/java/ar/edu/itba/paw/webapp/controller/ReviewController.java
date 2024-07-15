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
    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private UserService userService;
    @Context
    private UriInfo uriInfo;

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

        int lastPage = reviewService.getTotalPages(userId, subjectId);
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
        reviewService.deleteReviewVote(reviewId, userId);

        return Response.noContent().build();
    }

}
