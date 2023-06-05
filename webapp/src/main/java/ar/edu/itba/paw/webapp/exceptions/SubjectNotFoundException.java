package ar.edu.itba.paw.webapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SubjectNotFoundException extends RuntimeException {
    public SubjectNotFoundException() {
        super();
    }

    public SubjectNotFoundException(String message) {
        super(message);
    }

    public SubjectNotFoundException(Throwable cause) {
        super(cause);
    }

    public SubjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
