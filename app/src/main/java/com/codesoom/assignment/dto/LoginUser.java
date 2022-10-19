package com.codesoom.assignment.dto;

/**
 * 토큰에 담긴 회원 정보
 */
public class LoginUser {
    private final Long id;

    public LoginUser(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

