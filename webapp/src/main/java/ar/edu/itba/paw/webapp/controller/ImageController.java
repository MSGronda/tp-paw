package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.exceptions.ImageNotFoundException;
import ar.edu.itba.paw.services.ImageService;
import ar.edu.itba.paw.webapp.controller.utils.CacheHelper;
import ar.edu.itba.paw.webapp.controller.utils.UriUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.Duration;

@Path("images")
@Component
public class ImageController {
    private static final int MAX_CACHE_AGE = (int) Duration.ofDays(365).getSeconds();

    private final ImageService imgService;
    @Context
    private UriInfo uriInfo;

    @Autowired
    public ImageController(final ImageService imgService){
        this.imgService = imgService;
    }


    @GET
    @Path("/{id}")
    @Produces({"image/jpeg", "image/png"})
    public Response getImage(@PathParam("id") final long id) {
        final byte[] img = imgService.findById(id).map(Image::getImage).orElseThrow(ImageNotFoundException::new);

        return Response.ok(img).build();
    }

    @POST
    @Consumes({"image/jpeg", "image/png"})
    @Produces("application/vnd.image.v1+json")
    public Response uploadImage(final byte[] picture){
        final Image image = imgService.createImage(picture);

        return Response.created(UriUtils.createdImageUri(uriInfo, image)).build();
    }
}
