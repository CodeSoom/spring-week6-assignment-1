package com.codesoom.assignment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserLoginData {
    private String email;

    private String password;

    @Builder
    public UserLoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserLoginData(String email) {
        this.email = email;
    }
}
