package com.fineapple.Infrastructure.exception;

public class DataUpdateException extends RuntimeException {
    public DataUpdateException(String message) {
        super(message);
    }
}