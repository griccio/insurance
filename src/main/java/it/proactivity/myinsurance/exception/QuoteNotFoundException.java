package it.proactivity.myinsurance.exception;

public class QuoteNotFoundException extends RuntimeException{
    public QuoteNotFoundException(String message) {
        super(message);
    }

    public QuoteNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuoteNotFoundException(Throwable cause) {
        super(cause);
    }
}
