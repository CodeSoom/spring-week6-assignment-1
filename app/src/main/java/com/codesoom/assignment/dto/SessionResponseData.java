package com.codesoom.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 로그인 후 생성된 JWT DTO
 */
@Getter
@Builder
@AllArgsConstructor
public class SessionResponseData {
    private String accessToken;
}
