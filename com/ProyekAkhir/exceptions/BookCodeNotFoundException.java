package com.ProyekAkhir.exceptions;

public class BookCodeNotFoundException extends Exception {
    public BookCodeNotFoundException(String message) {
        super(message);
    }

    public BookCodeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return "ERROR BookCodeNotFoundException: " + getMessage();
    }
}
