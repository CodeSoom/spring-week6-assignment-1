package com.codesoom.assignment.dto;

import io.jsonwebtoken.Claims;
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

    @Builder
    public AuthenticationResultData(String email) {
        this.email = email;
    }

    public static AuthenticationResultData of(Claims claims) {
        return AuthenticationResultData.builder()
                .email(claims.getSubject())
                .build();
    }
}
