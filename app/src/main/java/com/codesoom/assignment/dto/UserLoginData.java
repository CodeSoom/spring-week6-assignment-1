package com.codesoom.assignment.dto;

import javax.validation.constraints.NotBlank;

public class UserLoginData {
    @NotBlank
    private final String email;

    @NotBlank
    private final String password;

    public UserLoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
