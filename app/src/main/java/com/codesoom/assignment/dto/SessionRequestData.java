package com.codesoom.assignment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class SessionRequestData {
    private String email;
    private String name;
    private String password;

    protected SessionRequestData() {
    }

    private SessionRequestData(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
}
