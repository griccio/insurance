package it.proactivity.myinsurance.exception;

public class HolderNotFoundException  extends RuntimeException{
    public HolderNotFoundException(String message) {
        super(message);
    }

    public HolderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public HolderNotFoundException(Throwable cause) {
        super(cause);
    }
}
