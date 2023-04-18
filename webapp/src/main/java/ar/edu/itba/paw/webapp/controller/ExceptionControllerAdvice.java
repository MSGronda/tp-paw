package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.exceptions.DegreeNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException e) {
        return "error/page_not_found";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SubjectNotFoundException.class)
    public String handleSubjectNotFoundException(SubjectNotFoundException e) {
        return "error/page_not_found";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DegreeNotFoundException.class)
    public String handleDegreeNotFoundException(DegreeNotFoundException e) {
        return "error/page_not_found";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handlePageNotFoundException(NoHandlerFoundException e) {
        e.printStackTrace();
        return "error/page_not_found";
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        e.printStackTrace();
        return "error/internal_error";
    }
}
