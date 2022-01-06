package com.codesoom.assignment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginRequestData {
    private String email;

    private String password;

    @Builder
    public LoginRequestData(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
