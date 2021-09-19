package com.codesoom.assignment.errors;

import com.codesoom.assignment.dto.UserLoginData;

/**
 * 이메일 검색 실패 예외
 */
public class EmailNotFoundException extends RuntimeException{
    public EmailNotFoundException(UserLoginData loginData){
        super("Email not found:" +loginData.getEmail());
    }
}
