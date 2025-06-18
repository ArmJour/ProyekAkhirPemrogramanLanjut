package com.ProyekAkhir.exceptions;

public class FeeAlreadyPaidOrDoesNotExists extends Exception {
    public FeeAlreadyPaidOrDoesNotExists(String message) {
        super(message);
    }

    public FeeAlreadyPaidOrDoesNotExists(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return "ERROR FeeAlreadyPaidOrDoesNotExists: " + getMessage();
    }
}
