package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.InvalidImageSizeException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidImageSizeExceptionMapper implements ExceptionMapper<InvalidImageSizeException> {
    @Override
    public Response toResponse(InvalidImageSizeException e) {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
