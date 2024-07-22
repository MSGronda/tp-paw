package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.services.DepartmentService;
import ar.edu.itba.paw.webapp.dto.DepartmentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("departments")
@Component
public class DepartmentController {
    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        final List<DepartmentDto> departments = departmentService.getAll()
            .stream().map(DepartmentDto::fromString)
            .collect(Collectors.toList());
        
        if(departments.isEmpty()) return Response.noContent().build();
        
        return Response.ok(new GenericEntity<List<DepartmentDto>>(departments){}).build();
    }
}
