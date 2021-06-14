package com.codesoom.assignment.errors;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException(String email) {
        super("Your Password Wrong  : "+email);
    }
}
