package ar.edu.itba.paw.models.exceptions;

public class InvalidFormException extends RuntimeException {
    public InvalidFormException() {
        super();
    }
    public InvalidFormException(String message) {
        super(message);
    }
    public InvalidFormException(Exception e) {
        super(e);
    }
}
