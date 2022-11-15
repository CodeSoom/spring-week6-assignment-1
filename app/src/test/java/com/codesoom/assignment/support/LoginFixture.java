package com.codesoom.assignment.support;

import com.codesoom.assignment.session.dto.SessionRequestDto;

public enum LoginFixture {
    LOGIN_VALID("dev.gibeom@gmail.com", "비밀번호486"),
    LOGIN_INVALID("dev.gibeom@gmail.com", "password486"),
    ;

    private String email;
    private String password;

    LoginFixture(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String EMAIL() {
        return email;
    }

    public String PASSWORD() {
        return password;
    }

    public SessionRequestDto 로그인_요청_데이터_생성() {
        return SessionRequestDto.builder()
                .email(this.email)
                .password(this.password)
                .build();
    }
}
