package com.codesoom.assignment.application;

import com.codesoom.assignment.dto.AuthenticationCreateData;
import com.codesoom.assignment.dto.AuthenticationResultData;
import com.codesoom.assignment.dto.SessionResultData;
import com.codesoom.assignment.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/** 사용자 인증에 대한 요청을 수행한다. */
@Service
@Transactional
public class AuthenticationService {
    private final JwtUtil jwtUtil;

    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 주어진 사용자를 검증하고 토큰 문자열을 생성하여 리턴한다.
     *
     * @param authenticationCreateData - 검증하고자 하는 사용자
     * @return 주어진 사용자를 이용하여 생성된 토큰 문자열
     */
    public SessionResultData createToken(AuthenticationCreateData authenticationCreateData) {
        String accessToken = jwtUtil.encode(
                authenticationCreateData.getEmail()
                ,authenticationCreateData.getPassword()
        );

        return SessionResultData.builder()
                .accessToken(accessToken)
                .build();
    }

    /**
     * 주어진 토큰 문자열을 파싱하여 사용자 정보를 리넌한다.
     *
     * @param accessToken - 파싱하고자 하는 토큰 문자열
     * @return 주어진 {@code accessToken}의 사용자 정보
     */
    public AuthenticationResultData parseToken(String accessToken) {
        Claims claims = jwtUtil.decode(accessToken);

        return AuthenticationResultData.builder()
                .email(claims.get("email", String.class))
                .password((claims.get("password", String.class)))
                .build();
    }
}
