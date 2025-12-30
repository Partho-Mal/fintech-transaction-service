//GlobalExceptionHandler
//handles IdempotencyViolationException
//returns HTTP conflict
//returns JSON error

package com.example.fintech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IdempotencyViolationException.class)
    public ResponseEntity<Map<String, Object>> handleIdempotencyViolation(IdempotencyViolationException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "timestamp", Instant.now(),
                        "error", "IDEMPOTENCY_VIOLATION",
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(ConcurrencyFailureException.class)
    public ResponseEntity<Map<String, Object>> handleConcurrencyFailure(
            ConcurrencyFailureException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "timestamp", Instant.now(),
                        "error", "CONCURRENT_MODIFICATION",
                        "message", ex.getMessage()
                ));
    }

}
