package com.codesoom.assignment.dto;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
