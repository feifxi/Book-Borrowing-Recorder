package org.example.domain.exception;

public class InvalidBookException extends  RuntimeException{
    public InvalidBookException(){
        super();
    }
    public InvalidBookException(String message) {
        super(message);
    }
    public InvalidBookException(Throwable cause) {
        super(cause);
    }
    public InvalidBookException(String message, Throwable cause) {
        super(message, cause);
    }
}
