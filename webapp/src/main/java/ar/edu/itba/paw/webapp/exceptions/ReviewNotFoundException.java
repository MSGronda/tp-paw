package ar.edu.itba.paw.webapp.exceptions;

public class ReviewNotFoundException extends RuntimeException{
    public ReviewNotFoundException(){
        super();
    }

    public ReviewNotFoundException(String message){
        super(message);
    }
}
