package com.codesoom.assignment.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 사용자 인증 요청.
 */
@Getter
@NoArgsConstructor
public class LoginRequest {
    /**
     * 이메일.
     */
    @NotBlank
    @Size(min = 3)
    private String email;

    /** 비밀번호. */
    @NotNull
    @Size(min = 4, max = 1024)
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
