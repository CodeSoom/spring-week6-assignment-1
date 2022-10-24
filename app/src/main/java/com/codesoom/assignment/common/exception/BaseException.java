package com.codesoom.assignment.common.exception;

import com.codesoom.assignment.common.response.ErrorCode;
import lombok.Generated;
import lombok.Getter;

@Generated
@Getter
public class BaseException extends RuntimeException{

    private ErrorCode errorCode;

    public BaseException() {
    }

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getErrorMsg());
        this.errorCode = errorCode;
    }

    public BaseException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BaseException(String message, ErrorCode errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

}
