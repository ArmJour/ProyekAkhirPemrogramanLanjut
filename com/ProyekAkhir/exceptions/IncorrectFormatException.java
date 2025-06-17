package com.ProyekAkhir.exceptions;

public class IncorrectFormatException extends Exception {
    public IncorrectFormatException(String message) {
        super(message);
    }

    public IncorrectFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return "ERROR IncorrectFormatException: " + getMessage();
    }
}
