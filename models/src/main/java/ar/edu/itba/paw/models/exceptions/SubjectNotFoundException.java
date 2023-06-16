package ar.edu.itba.paw.models.exceptions;

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
