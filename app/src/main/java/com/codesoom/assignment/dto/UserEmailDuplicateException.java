package com.codesoom.assignment.dto;

public class UserEmailDuplicateException extends Exception{

    public UserEmailDuplicateException() {

        super("이미 가입된 메일이 존재합니다.");

    }

}

