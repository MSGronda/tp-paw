package ar.edu.itba.paw.webapp.controller.utils;

import ar.edu.itba.paw.models.*;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class UriUtils {
    public static final String DEGREE_BASE = "degrees";
    public static final String DEPARTMENT_BASE = "departments";
    public static final String IMAGE_BASE = "images";
    public static final String PROFESSOR_BASE = "professors";
    public static final String REVIEW_BASE = "reviews";
    public static final String SUBJECT_BASE = "subjects";
    public static final String USER_BASE = "users";

    public static URI createdDegreeUri(final UriInfo uriInfo, final Degree degree){
        return uriInfo.getBaseUriBuilder().path(DEGREE_BASE).path(String.valueOf(degree.getId())).build();
    }

    public static URI createdDegreeSemestersUri(final UriInfo uriInfo, final Degree degree){
        return uriInfo.getBaseUriBuilder().path(DEGREE_BASE).path(String.valueOf(degree.getId())).path("semesters").build();
    }

    public static URI createdImageUri(final UriInfo uriInfo, final Image image){
        return uriInfo.getBaseUriBuilder().path(IMAGE_BASE).path(String.valueOf(image.getId())).build();
    }

    public static URI createdProfessorUri(final UriInfo uriInfo, final Professor professor){
        return uriInfo.getBaseUriBuilder().path(PROFESSOR_BASE).path(String.valueOf(professor.getId())).build();
    }

    public static URI createdReviewUri(final UriInfo uriInfo, final Review review){
        return uriInfo.getBaseUriBuilder().path(REVIEW_BASE).path(String.valueOf(review.getId())).build();
    }

    public static URI createdReviewVoteUri(final UriInfo uriInfo, final ReviewVote reviewVote){
        return uriInfo.getBaseUriBuilder().path(REVIEW_BASE).path(String.valueOf(reviewVote.getReview().getId()))
                .path("votes").queryParam("userId", reviewVote.getUser().getId()).build();
    }

    public static URI createdSubjectUri(final UriInfo uriInfo, final Subject subject){
        return uriInfo.getBaseUriBuilder().path(SUBJECT_BASE).path(String.valueOf(subject.getId())).build();
    }

    public static URI createdUserUri(final UriInfo uriInfo, final User user){
        return uriInfo.getBaseUriBuilder().path(USER_BASE).path(String.valueOf(user.getId())).build();
    }

    public static URI createdUserSemesterUri(final UriInfo uriInfo, final User user){
        return uriInfo.getBaseUriBuilder().path(USER_BASE).path(String.valueOf(user.getId())).path("plan").build();
    }
}
