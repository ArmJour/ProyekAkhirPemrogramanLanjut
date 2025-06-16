package com.ProyekAkhir.exceptions;

public class DataAlreadyExistsExceptions extends Exception {
    public DataAlreadyExistsExceptions(String message) {
        super(message);
    }

    public DataAlreadyExistsExceptions(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return "ERROR DataAlreadyExistsExceptions: " + getMessage();
    }
}
