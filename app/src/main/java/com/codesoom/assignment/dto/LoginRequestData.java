package com.codesoom.assignment.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequestData {

    private String email;
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
