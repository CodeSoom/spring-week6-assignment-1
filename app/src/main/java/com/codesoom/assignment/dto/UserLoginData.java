package com.codesoom.assignment.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserLoginData {
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    @Size(min = 4)
    private String password;
}
