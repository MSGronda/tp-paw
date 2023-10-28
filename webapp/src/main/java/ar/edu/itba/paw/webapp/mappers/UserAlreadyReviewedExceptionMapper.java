package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.UserAlreadyReviewedException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UserAlreadyReviewedExceptionMapper implements ExceptionMapper<UserAlreadyReviewedException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(UserAlreadyReviewedException e) {
        return Response.status(Response.Status.CONFLICT).build();
    }
}
