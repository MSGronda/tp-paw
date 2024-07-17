package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.InvalidPageNumberException;
import ar.edu.itba.paw.models.exceptions.InvalidTokenException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;

public class InvalidTokenExceptionMapper implements ExceptionMapper<InvalidTokenException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(InvalidTokenException e) {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
