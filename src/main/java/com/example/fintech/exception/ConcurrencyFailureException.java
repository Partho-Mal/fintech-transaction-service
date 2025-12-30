package com.example.fintech.exception;

public class ConcurrencyFailureException extends RuntimeException {

    public ConcurrencyFailureException(String message) {
        super(message);
    }
}
