package com.fineapple.application.exception;

public class OrderItemDetailNotFoundException extends RuntimeException {
    public OrderItemDetailNotFoundException(String message) {
        super(message);
    }

}
