package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            DegreeNotFoundException.class,
            ImageNotFoundException.class,
            ReviewNotFoundException.class,
            SubjectNotFoundException.class,
            UserNotFoundException.class
    })
    public String notFoundHandler() {
        return "error/page_not_found";
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UnauthorizedException.class)
    public String forbiddenHandler() {
        return "error/unauthorized";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            InvalidFormException.class,
            InvalidPageNumberException.class
    })
    public String badRequestHandler() {
        return "error/bad_request";
    }
}
