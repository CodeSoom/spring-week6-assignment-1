package com.codesoom.assignment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserLoginData {

    @NotBlank
    String email;

    @NotBlank
    String password;

    @Builder
    public UserLoginData(String email, String password) {
        this.email = email;
        this.password = password;

    }

}
