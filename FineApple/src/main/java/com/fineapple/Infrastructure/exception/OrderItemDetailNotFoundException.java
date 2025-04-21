package com.fineapple.Infrastructure.exception;

public class OrderItemDetailNotFoundException extends RuntimeException {
    public OrderItemDetailNotFoundException(String message) {
        super(message);
    }

}
