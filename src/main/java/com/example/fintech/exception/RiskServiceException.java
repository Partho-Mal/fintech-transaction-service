//RuntimeException
//Thrown when risk service fails badly

package com.example.fintech.exception;

public class RiskServiceException extends RuntimeException {

    public RiskServiceException(String message) {
        super(message);
    }
}
