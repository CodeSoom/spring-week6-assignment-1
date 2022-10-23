package com.codesoom.assignment.exception;

/**
 * 주문을 찾을 수 없을 때 던집니다.
 */
public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
