package com.fineapple.Infrastructure.exception;


public class PredictServiceException extends RuntimeException {
    public PredictServiceException(String message) {
        super(message);
    }
}
