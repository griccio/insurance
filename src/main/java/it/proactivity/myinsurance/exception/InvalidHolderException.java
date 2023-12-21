package it.proactivity.myinsurance.exception;

public class InvalidHolderException extends RuntimeException{
    public InvalidHolderException(String message) {
        super(message);
    }

    public InvalidHolderException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidHolderException(Throwable cause) {
        super(cause);
    }
}
