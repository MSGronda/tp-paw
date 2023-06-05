package ar.edu.itba.paw.webapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DegreeNotFoundException extends RuntimeException{
    public DegreeNotFoundException() {
        super();
    }

    public DegreeNotFoundException(String message) {
        super(message);
    }
}
