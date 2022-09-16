package com.codesoom.assignment.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class ErrorResponse {
    private String message;
    private HttpStatus status;
    private List<String> errors;

    public ErrorResponse(String message) {
        this.message = message;
    }

    @Builder
    public ErrorResponse(String message, HttpStatus status, List<String> errors) {
        this.message = message;
        this.status = status;
        this.errors = errors;
    }
}
