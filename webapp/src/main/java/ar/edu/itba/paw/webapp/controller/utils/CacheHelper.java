package ar.edu.itba.paw.webapp.controller.utils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.CacheControl;
import java.time.Duration;

public class CacheHelper {
    public static Response.ResponseBuilder setUnconditionalCache(final Response.ResponseBuilder responseBuilder, final int max_age) {
        final CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(max_age);
        responseBuilder.cacheControl(cacheControl);
        return responseBuilder;
    }

}
