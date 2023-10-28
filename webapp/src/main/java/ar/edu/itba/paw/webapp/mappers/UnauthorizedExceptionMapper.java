package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.UnauthorizedException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;

public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(UnauthorizedException e) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
