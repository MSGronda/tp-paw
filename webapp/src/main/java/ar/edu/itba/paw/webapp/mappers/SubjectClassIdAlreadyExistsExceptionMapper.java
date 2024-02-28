package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.SubjectClassIdAlreadyExistsException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SubjectClassIdAlreadyExistsExceptionMapper implements ExceptionMapper<SubjectClassIdAlreadyExistsException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(SubjectClassIdAlreadyExistsException e) {
        return Response.status(Response.Status.CONFLICT).build();
    }
}
