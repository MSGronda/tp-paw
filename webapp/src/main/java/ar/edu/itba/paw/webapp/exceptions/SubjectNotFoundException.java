package ar.edu.itba.paw.webapp.exceptions;

public class SubjectNotFoundException extends RuntimeException {
    public SubjectNotFoundException() {
        super();
    }

    public SubjectNotFoundException(String message) {
        super(message);
    }
}
