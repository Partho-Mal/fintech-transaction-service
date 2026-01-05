package com.example.fintech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // Returns 409 for optimistic lock failures
public class ConcurrencyFailureException extends RuntimeException {

    public ConcurrencyFailureException(String message) {
        super(message);
    }
}
