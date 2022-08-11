package com.codesoom.assignment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SessionRegistrationData {
    private String email;
    private String password;

    public SessionRegistrationData(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
