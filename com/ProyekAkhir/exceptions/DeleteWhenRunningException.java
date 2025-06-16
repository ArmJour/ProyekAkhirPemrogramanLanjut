package com.ProyekAkhir.exceptions;

public class DeleteWhenRunningException extends Exception {
    public DeleteWhenRunningException(String message) {
        super(message);
    }

    public DeleteWhenRunningException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return "ERROR DeleteWhenRunningException: " + getMessage();
    }
}