package com.ProyekAkhir.exceptions;

public class ReturnBookNotBorrowed extends Exception {
    public ReturnBookNotBorrowed(String message) {
        super(message);
    }

    public ReturnBookNotBorrowed(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return "ERROR ReturnBookNotBorrowed: " + getMessage();
    }
}
