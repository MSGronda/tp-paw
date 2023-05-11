package ar.edu.itba.paw.webapp.exceptions;

public class RoleNotFoundException extends RuntimeException{
    public RoleNotFoundException() {
        super();
    }

    public RoleNotFoundException(String message) {
        super(message);
    }
}
