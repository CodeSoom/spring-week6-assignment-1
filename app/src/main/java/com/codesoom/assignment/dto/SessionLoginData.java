package com.codesoom.assignment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@ToString
public class SessionLoginData {

    @NotBlank
    private String  email;

    @NotBlank
    @Size(min = 4, max = 1024)
    private String password;

    @Builder
    public SessionLoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
