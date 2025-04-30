package com.fineapple.application.exception;

public class InvalidNameException extends UserRegistrationException {
    public InvalidNameException(String message) {
        super(message);
    }
}
