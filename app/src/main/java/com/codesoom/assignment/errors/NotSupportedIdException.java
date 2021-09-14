package com.codesoom.assignment.errors;

public class NotSupportedIdException extends RuntimeException {
    public NotSupportedIdException(Long id) {
        super("Not Supported Id: " + id);
    }
}
