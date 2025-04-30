package com.fineapple.application.exception;

public class InvalidPasswordException extends UserRegistrationException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}