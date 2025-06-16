package com.ProyekAkhir.exceptions;

public class NIMNotFoundException extends Exception {
    public NIMNotFoundException(String message) {
        super(message);
    }

    public NIMNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return "ERROR NIMNotFoundException: " + getMessage();
    }
}