package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.DegreeNotFoundException;
import ar.edu.itba.paw.models.exceptions.InvalidFormException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidFormExceptionMapper implements ExceptionMapper<InvalidFormException> {
    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(InvalidFormException e) {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
