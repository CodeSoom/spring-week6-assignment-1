package com.codesoom.assignment.errors;

import com.codesoom.assignment.errors.ExceptionEnum.ExceptionMessage;

public class LoginNotMatchException extends MyException{
    private static final String MESSAGE = ExceptionMessage.LOGINMESSAGE.getMessage();


    public LoginNotMatchException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
