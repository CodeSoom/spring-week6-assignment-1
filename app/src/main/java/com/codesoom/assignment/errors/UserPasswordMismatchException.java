package com.codesoom.assignment.errors;

public class UserPasswordMismatchException extends RuntimeException {
    public UserPasswordMismatchException() {
        super("Login failed. Check user's login data.");
    }
}
