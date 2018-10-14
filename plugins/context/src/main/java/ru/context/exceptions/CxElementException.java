package ru.context.exceptions;

public class CxElementException extends RuntimeException {
    public CxElementException() {
    }

    public CxElementException(String message) {
        super(message);
    }

    public CxElementException(String message, Throwable cause) {
        super(message, cause);
    }

    public CxElementException(Throwable cause) {
        super(cause);
    }
}
