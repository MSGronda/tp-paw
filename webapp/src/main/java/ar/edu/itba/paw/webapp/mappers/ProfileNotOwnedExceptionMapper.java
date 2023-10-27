package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.ProfileNotOwnedException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ProfileNotOwnedExceptionMapper implements ExceptionMapper<ProfileNotOwnedException> {
    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(ProfileNotOwnedException e) {
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
