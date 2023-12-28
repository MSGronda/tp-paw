package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.ImageNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ImageNotFoundExceptionMapper implements ExceptionMapper<ImageNotFoundException> {
    @Override
    public Response toResponse(ImageNotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
