package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.exceptions.InvalidTokenException;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("verification-token")
@Component
public class ConfirmController {
    @Autowired
    private UserService userService;
    
    @POST
    public Response confirm(@RequestBody String token) {
        try {
            userService.confirmUser(token);
            return Response.ok().build();
        } catch (InvalidTokenException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
