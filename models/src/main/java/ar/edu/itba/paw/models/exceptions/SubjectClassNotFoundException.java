package ar.edu.itba.paw.models.exceptions;

public class SubjectClassNotFoundException extends RuntimeException {
    public SubjectClassNotFoundException(){
        super();
    }

    public SubjectClassNotFoundException(String message){
        super(message);
    }
}
