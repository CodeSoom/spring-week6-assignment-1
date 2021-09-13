package com.codesoom.assignment.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class LoginForm {

    @NotBlank
    @Size(min = 3)
    private String email;

    @NotBlank
    @Size(min = 4, max = 1024)
    private String password;
}
