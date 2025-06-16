package com.ProyekAkhir.exceptions;

public class InputKosongException extends Exception {
    public InputKosongException(String message) {
        super(message);
    }

    public InputKosongException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return "ERROR InputKosongException: " + getMessage();
    }
}