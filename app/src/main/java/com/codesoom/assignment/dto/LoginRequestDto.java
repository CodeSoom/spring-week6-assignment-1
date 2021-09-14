package com.codesoom.assignment.dto;

import com.github.dozermapper.core.Mapping;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginRequestDto {

    @NotBlank
    @Mapping("email")
    private final String email;

    @NotBlank
    @Mapping("password")
    private final String password;

    @Builder
    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
