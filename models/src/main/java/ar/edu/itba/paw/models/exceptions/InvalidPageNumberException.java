package ar.edu.itba.paw.models.exceptions;

public class InvalidPageNumberException extends RuntimeException {
    public InvalidPageNumberException(){
        super();
    }

    public InvalidPageNumberException(String message){
        super(message);
    }
}
