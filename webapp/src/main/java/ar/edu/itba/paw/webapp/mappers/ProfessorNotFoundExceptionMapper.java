package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.ProfessorNotFoundException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;

public class ProfessorNotFoundExceptionMapper  implements ExceptionMapper<ProfessorNotFoundException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(ProfessorNotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
