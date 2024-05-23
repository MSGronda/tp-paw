package ar.edu.itba.paw.webapp.controller.utils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.CacheControl;
import java.time.Duration;

public class CacheHelper {
    private static final int MAX_AGE = (int) Duration.ofDays(1).getSeconds();

    public static void setUnconditionalCache(final Response.ResponseBuilder responseBuilder) {
        final CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(MAX_AGE);
        responseBuilder.cacheControl(cacheControl);
    }

}
