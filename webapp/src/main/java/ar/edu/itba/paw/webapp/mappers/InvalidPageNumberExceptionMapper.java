package ar.edu.itba.paw.webapp.mappers;


import ar.edu.itba.paw.models.exceptions.InvalidPageNumberException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidPageNumberExceptionMapper implements ExceptionMapper<InvalidPageNumberException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(InvalidPageNumberException e) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}