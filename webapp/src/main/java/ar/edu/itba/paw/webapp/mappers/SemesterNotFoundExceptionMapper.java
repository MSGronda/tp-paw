package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.models.exceptions.SemesterNotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SemesterNotFoundExceptionMapper implements ExceptionMapper<SemesterNotFoundException> {

        @Context
        private UriInfo uriInfo;

        @Override
        public Response toResponse(SemesterNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
}
