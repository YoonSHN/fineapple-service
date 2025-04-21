package com.fineapple.Infrastructure.exception;

public class InvalidPhoneNumberException extends UserRegistrationException{
    public InvalidPhoneNumberException(String message) {
        super(message);
    }
}
