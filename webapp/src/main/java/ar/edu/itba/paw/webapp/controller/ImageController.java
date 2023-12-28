package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.exceptions.ImageNotFoundException;
import ar.edu.itba.paw.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("images")
@Component
public class ImageController {
    private final ImageService imgService;

    @Autowired
    public ImageController(final ImageService imgService) {
        this.imgService = imgService;
    }

    @GET
    @Path("/{id}")
    @Produces({"image/jpeg", "image/png"})
    public Response image(@PathParam("id") final long id) {
        final byte[] img = imgService.findById(id).map(Image::getImage).orElseThrow(ImageNotFoundException::new);
        
        return Response.ok(img).build();
    }
}
