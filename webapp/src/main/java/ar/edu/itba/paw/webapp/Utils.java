package ar.edu.itba.paw.webapp;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;

public class Utils {
    private static final UrlPathHelper urlPathHelper = new UrlPathHelper();

    private Utils(){}

    public static String getBaseUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
