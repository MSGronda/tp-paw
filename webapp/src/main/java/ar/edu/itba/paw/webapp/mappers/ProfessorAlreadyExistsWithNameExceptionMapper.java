package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.ProfessorAlreadyExistsWithNameException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ProfessorAlreadyExistsWithNameExceptionMapper implements ExceptionMapper<ProfessorAlreadyExistsWithNameException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(ProfessorAlreadyExistsWithNameException e) {
        return Response.status(Response.Status.CONFLICT).build();
    }
}

