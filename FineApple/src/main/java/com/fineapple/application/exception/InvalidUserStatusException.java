package com.fineapple.application.exception;

public class InvalidUserStatusException extends RuntimeException {
    public InvalidUserStatusException(String message) {
        super(message);
    }
}