package com.fineapple.Infrastructure.exception;

public class InvalidNameException extends UserRegistrationException {
    public InvalidNameException(String message) {
        super(message);
    }
}
