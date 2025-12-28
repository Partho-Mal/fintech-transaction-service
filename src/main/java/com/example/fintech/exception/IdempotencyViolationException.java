//IdempotencyViolationException
//extends RuntimeException
//constructor with message

package com.example.fintech.exception;

public class IdempotencyViolationException extends RuntimeException {

    public IdempotencyViolationException(String message){
        super(message);
    }
}
