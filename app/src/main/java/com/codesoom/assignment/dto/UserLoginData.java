package com.codesoom.assignment.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class UserLoginData {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
