package com.codesoom.assignment.errors;

public class InvalidUserDataException extends RuntimeException{
    public InvalidUserDataException(String email) {
            super("Invalid User information : " + email);
        }
}
