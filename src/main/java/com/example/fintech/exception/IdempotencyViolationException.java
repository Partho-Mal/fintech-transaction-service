//IdempotencyViolationException
//extends RuntimeException
//constructor with message

package com.example.fintech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // Returns 409 if key exists
public class IdempotencyViolationException extends RuntimeException {

    public IdempotencyViolationException(String message) {
        super(message);
    }
}
