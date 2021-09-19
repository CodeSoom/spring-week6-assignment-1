package com.codesoom.assignment.errors;

public class NotSupportedUserIdException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "Not Supported UserId: ";

    public NotSupportedUserIdException(Long id) {
        super(DEFAULT_MESSAGE + id);
    }
}
