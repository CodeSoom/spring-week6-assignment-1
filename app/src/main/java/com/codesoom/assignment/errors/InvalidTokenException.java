package com.codesoom.assignment.errors;

import com.codesoom.assignment.errors.ExceptionEnum.ExceptionMessage;

public class InvalidTokenException extends MyException{
    private static final String MESSAGE = ExceptionMessage.InValidToken.getMessage();


    public InvalidTokenException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
