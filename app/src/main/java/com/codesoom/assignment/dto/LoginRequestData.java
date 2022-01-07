package com.codesoom.assignment.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class LoginRequestData {
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @Builder
    public LoginRequestData(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
