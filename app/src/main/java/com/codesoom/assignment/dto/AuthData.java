package com.codesoom.assignment.dto;

import lombok.Builder;
import lombok.Getter;

public class AuthData {
    @Getter
    private String email;

    @Getter
    private String password;

    public AuthData () {}

    @Builder
    public AuthData(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
