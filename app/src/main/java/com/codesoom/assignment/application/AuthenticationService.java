package com.codesoom.assignment.application;

import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;

/**
 * 인증과 관련된 작업을 제공 한다.
 */
@Service
public class AuthenticationService {

    private JwtUtil jwtUtil;

    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 로그인을 올바른 정보로 요청하면 토큰 값이 반환되고, 아니면 예외가 던져진다.
     * @return 암호화된 내용
     */
    public String login() {
        return jwtUtil.encode(1L);
    }

    /**
     * 암호화된 내용을 평문으로 반환한다.
     * @param accessToken
     * @return 평문
     */
    public Long parseToken(String accessToken) {
        if (accessToken == null || accessToken.isBlank()) {
            throw new InvalidAccessTokenException(accessToken);
        }

        try {
            Claims claims = jwtUtil.decode(accessToken);
            return claims.get("userId", Long.class);
        } catch (SignatureException e) {
            throw new InvalidAccessTokenException(accessToken);
        }
    }
}
