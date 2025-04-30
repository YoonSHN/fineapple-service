package com.fineapple.application.exception;

public class ProductUploadException extends RuntimeException {
    public ProductUploadException(String message) {
        super(message);
    }

    public ProductUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
