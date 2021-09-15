package com.codesoom.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class LoginForm implements Identifier {

    @NotBlank
    @Size(min = 3)
    private String email;

    @NotBlank
    @Size(min = 4, max = 1024)
    private String password;

    public static LoginForm of(String email, String password) {
        return new LoginForm(email, password);
    }
}
