package com.codesoom.assignment.common.exception;

/**
 * 입력이 되지 않은 필수항목이 있을경우 던집니다.
 */
public class InvalidParamException extends RuntimeException {
    public InvalidParamException(String errorMsg) {
        super(errorMsg);
    }
}
