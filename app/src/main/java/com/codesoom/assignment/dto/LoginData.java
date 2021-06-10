package com.codesoom.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 로그인 데이터 DTO
 */
@Getter
@Builder
@AllArgsConstructor
public class LoginData {
    private String email;
    private String password;
}
