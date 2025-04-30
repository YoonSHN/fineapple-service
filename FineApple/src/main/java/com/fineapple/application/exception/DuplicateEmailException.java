package com.fineapple.application.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException() {
        super("이미 등록된 이메일입니다.");
    }
}
