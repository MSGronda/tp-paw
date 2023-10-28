package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.ReviewNotFoundException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ReviewNotFoundExceptionMapper implements ExceptionMapper<ReviewNotFoundException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(ReviewNotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
