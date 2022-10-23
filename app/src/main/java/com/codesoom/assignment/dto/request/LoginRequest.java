package com.codesoom.assignment.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.beans.ConstructorProperties;

/**
 * 로그인 요청.
 */
@Getter
public class LoginRequest {

    @NotBlank(message = "이메일은 필수값입니다.")
    private final String email;

    @NotBlank(message = "비밀번호는 필수값입니다.")
    private final String password;

    @ConstructorProperties({"email", "password"})
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
