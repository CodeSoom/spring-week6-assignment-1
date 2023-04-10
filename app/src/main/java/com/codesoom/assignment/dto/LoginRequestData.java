package com.codesoom.assignment.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequestData {

    @Email
    @NotBlank
    private String email;
    
    @NotBlank
    private String password;

    @Builder
    public LoginRequestData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "{ \"email\": \""+email+"\", \"password\": \""+password+"\" }";
    }
}
