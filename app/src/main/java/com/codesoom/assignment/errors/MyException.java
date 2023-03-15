package com.codesoom.assignment.errors;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class MyException extends RuntimeException{
    public final Map<String, String> validation = new HashMap<>();

    public MyException(String message) {
        super(message);
    }

    public MyException(String message, Throwable cause) {
        super(message, cause);
    }


    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
