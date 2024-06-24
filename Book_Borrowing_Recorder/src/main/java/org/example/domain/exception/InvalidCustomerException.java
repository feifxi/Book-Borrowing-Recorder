package org.example.domain.exception;

public class InvalidCustomerException extends RuntimeException{
    public InvalidCustomerException(){
        super();
    }
    public InvalidCustomerException(String message) {
        super(message);
    }
    public InvalidCustomerException(Throwable cause) {
        super(cause);
    }
    public InvalidCustomerException(String message, Throwable cause) {
        super(message, cause);
    }
}