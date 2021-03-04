package com.codesoom.assignment.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class AuthenticationResultData {
    private String email;

    private String password;

    @Builder
    public AuthenticationResultData(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
