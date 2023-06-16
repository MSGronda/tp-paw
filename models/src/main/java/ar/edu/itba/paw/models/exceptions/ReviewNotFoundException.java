package ar.edu.itba.paw.models.exceptions;

public class ReviewNotFoundException extends RuntimeException{
    public ReviewNotFoundException(){
        super();
    }

    public ReviewNotFoundException(String message){
        super(message);
    }
}
