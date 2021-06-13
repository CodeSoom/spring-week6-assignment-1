package com.codesoom.assignment.dto;


import lombok.Getter;

/**
 * 요청한 세션 데이터를 받는 DTO
 */
@Getter
public class SessionRequestData {
    private String email;
    private String password;
}
