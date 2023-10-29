package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.InvalidUserSemesterIds;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidUserSemesterIdsMapper implements ExceptionMapper<InvalidUserSemesterIds> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(InvalidUserSemesterIds e) {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}