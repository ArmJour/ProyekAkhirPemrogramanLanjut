package com.ProyekAkhir.exceptions;

public class BukuSudahDipinjamException extends Exception {
    public BukuSudahDipinjamException(String message) {
        super(message);
    }

    public BukuSudahDipinjamException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return "ERROR BukuSudahDipinjamException: " + getMessage();
    }
}