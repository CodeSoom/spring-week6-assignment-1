package com.codesoom.assignment.errors;

public class UserDeleteException extends RuntimeException{
    public UserDeleteException(String email){
        super("This User is deleted: " + email);
    }
}
