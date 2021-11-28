package com.codesoom.assignment.dto;

import com.github.dozermapper.core.Mapping;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class UserLoginData {
    @NotBlank
    @Mapping("email")
    private String email;

    @NotBlank
    @Mapping("password")
    private String password;

    public UserLoginData() {};

    public UserLoginData(@NotBlank String email, @NotBlank String password) {
        this.email = email;
        this.password = password;
    }
}
