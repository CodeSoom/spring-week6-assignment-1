package com.codesoom.assignment.errors;

public class AccessTokenNotFoundException extends RuntimeException {
    
    public AccessTokenNotFoundException() {
        super("Access token not found");
    }
}
