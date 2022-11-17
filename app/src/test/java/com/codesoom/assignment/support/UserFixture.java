package com.codesoom.assignment.support;

import com.codesoom.assignment.domain.session.controller.dto.SessionRequestDto;
import com.codesoom.assignment.domain.user.domain.User;

public enum UserFixture {
    USER_1("기범", "dev.gibeom@gmail.com", "비밀번호486"),
    USER_2("Alex", "dev.gibeom@gmail.com", "password486"),
    USER_2_DIFFERENT_PASSWORD("Alex", "dev.gibeom@gmail.com", "비밀번호 틀렸지롱"),
    USER_NOT_REGISTER("등록되지 않은 회원이에요", "not_register@gmail.com", "password486"),
    USER_INVALID_NAME("", "name_invalid@gmail.com", "name.invalid"),
    USER_INVALID_BLANK_EMAIL("이메일이 유효하지 않아요", "     ", "email.invalid"),
    USER_INVALID_BLANK_PASSWORD("비밀번호가 유효하지 않아요", "password_invalid@gmail.com", "    "),
    ;

    private String name;
    private String email;
    private String password;

    UserFixture(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User 회원_엔티티_생성() {
        return 회원_엔티티_생성(null);
    }

    public User 회원_엔티티_생성(Long id) {
        return User.builder()
                .id(id)
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .build();
    }

    public SessionRequestDto 로그인_요청_데이터_생성() {
        return SessionRequestDto.builder()
                .email(this.email)
                .password(this.password)
                .build();
    }

    public String NAME() {
        return name;
    }

    public String EMAIL() {
        return email;
    }

    public String PASSWORD() {
        return password;
    }
}
