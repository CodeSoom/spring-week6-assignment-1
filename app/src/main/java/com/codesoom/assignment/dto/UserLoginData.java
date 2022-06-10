package com.codesoom.assignment.dto;

import com.github.dozermapper.core.Mapping;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLoginData {

    @NotBlank
    @Email
    @Mapping("email")
    private String email;

    @NotBlank
    @Mapping("password")
    private String password;

    @Builder
    public UserLoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
