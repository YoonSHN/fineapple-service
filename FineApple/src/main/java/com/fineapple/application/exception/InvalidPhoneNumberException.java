package com.fineapple.application.exception;

public class InvalidPhoneNumberException extends UserRegistrationException{
    public InvalidPhoneNumberException(String message) {
        super(message);
    }
}
