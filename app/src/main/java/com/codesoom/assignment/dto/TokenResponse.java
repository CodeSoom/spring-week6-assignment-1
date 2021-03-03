package com.codesoom.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 토큰 정보 응답.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class TokenResponse {
    private String token;
}
