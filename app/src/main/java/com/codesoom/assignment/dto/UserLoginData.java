package com.codesoom.assignment.dto;

import com.github.dozermapper.core.Mapping;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UserLoginData {
    @NotBlank
    @Mapping("email")
    private String email;

    @NotBlank
    @Mapping("password")
    private String password;

    protected UserLoginData() {}

    @Builder
    public UserLoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
