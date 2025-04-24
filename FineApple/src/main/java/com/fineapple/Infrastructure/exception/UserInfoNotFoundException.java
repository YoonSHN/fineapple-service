package com.fineapple.Infrastructure.exception;

public class UserInfoNotFoundException extends RuntimeException {
    public UserInfoNotFoundException() {
        super("UserInfo ID가 존재하지 않습니다.");
    }

    public UserInfoNotFoundException(String message) {
        super(message);
    }
}