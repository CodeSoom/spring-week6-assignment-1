package com.codesoom.assignment.exception;

/**
 * 재고가 부족할 때 던집니다.
 */
public class StockOutException extends RuntimeException {
    public StockOutException(String message) {
        super(message);
    }
}
