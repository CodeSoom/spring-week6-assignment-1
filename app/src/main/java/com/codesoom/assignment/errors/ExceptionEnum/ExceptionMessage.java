package com.codesoom.assignment.errors.ExceptionEnum;

public enum ExceptionMessage {
    InValidToken("Invalid Access Token"),
    LOGINMESSAGE("NOT MATCH LOGIN DATA"),
    ERROR("Error"),
    WARNING("Warning");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
