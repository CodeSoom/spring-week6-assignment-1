package com.codesoom.assignment.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * 사용자 인증 요청 객체
 */
@Getter
@NoArgsConstructor
public class AuthenticationRequestDto {
    /**
     * 이메일.
     */
    @Email
    private String email;

    /** 비밀번호. */
    @NotNull
    private String password;

    public AuthenticationRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
