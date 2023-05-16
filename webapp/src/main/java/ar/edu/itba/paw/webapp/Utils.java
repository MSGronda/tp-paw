package ar.edu.itba.paw.webapp;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class Utils {
    private Utils(){}

    public static String getBaseUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }
}
