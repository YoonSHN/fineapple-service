package com.fineapple.application.exception;

public class InvalidEmailException extends UserRegistrationException{
    public InvalidEmailException() {
        super("유효하지 않은 이메일 형식입니다.");
    }
}
