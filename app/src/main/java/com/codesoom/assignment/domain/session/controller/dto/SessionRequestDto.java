package com.codesoom.assignment.domain.session.controller.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@EqualsAndHashCode
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
