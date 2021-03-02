package com.codesoom.assignment.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class AuthenticationRequestDto {
    @Email
    private String email;

    @NotNull
    private String password;

    public AuthenticationRequestDto(@Email String email, @NotNull String password) {
        this.email = email;
        this.password = password;
    }
}
