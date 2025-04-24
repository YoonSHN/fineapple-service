package com.fineapple.Infrastructure.exception;

public class InvalidPasswordException extends UserRegistrationException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}