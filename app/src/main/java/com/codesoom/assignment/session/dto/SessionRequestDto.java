package com.codesoom.assignment.session.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class SessionRequestDto {
    @NotBlank(message = "이메일을 입력하세요")
    private final String email;

    @NotBlank(message = "비밀번호를 입력하세요")
    private final String password;

    @Builder
    public SessionRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
