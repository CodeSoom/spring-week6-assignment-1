package com.codesoom.assignment.dto;

import com.github.dozermapper.core.Mapping;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@lombok.Generated
@Getter
@NoArgsConstructor
public class UserLoginData {

    @NotBlank
    @Mapping("email")
    private String email;

    @NotBlank
    @Mapping("password")
    private String password;

    @Builder
    private UserLoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
