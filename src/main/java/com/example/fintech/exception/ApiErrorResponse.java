//timestamp
//error
//message

package com.example.fintech.exception;

import lombok.Getter;

import java.time.Instant;

@Getter
public class ApiErrorResponse {

    private final Instant timestamp;
    private final String error;
    private final String message;

    public ApiErrorResponse(String error, String message) {
        this.timestamp = Instant.now();
        this.error = error;
        this.message = message;
    }

}

