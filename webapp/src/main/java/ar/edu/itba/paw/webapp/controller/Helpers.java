package ar.edu.itba.paw.webapp.controller;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class Helpers {
    public static String getBaseUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }
}
