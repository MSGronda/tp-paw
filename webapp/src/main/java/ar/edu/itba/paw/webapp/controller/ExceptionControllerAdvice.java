package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.exceptions.DegreeNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.ImageNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionControllerAdvice {

    private static final String ERROR_PAGE = "error/page_not_found";

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException e) {
        return ERROR_PAGE;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SubjectNotFoundException.class)
    public String handleSubjectNotFoundException(SubjectNotFoundException e) {
        return ERROR_PAGE;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DegreeNotFoundException.class)
    public String handleDegreeNotFoundException(DegreeNotFoundException e) {
        return ERROR_PAGE;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ImageNotFoundException.class)
    public String handleImageNotFoundException(ImageNotFoundException e) {
        return ERROR_PAGE;
    }
}
