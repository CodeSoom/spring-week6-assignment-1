package com.codesoom.assignment.application;

import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.uitls.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;

/**
 * 회원정보에 대한 인증, 인증확인 처리를 담당합니다.
 */
@Service
public class AuthenticationService {
    private JwtUtil jwtUtil;

    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 회원정보를 인증합니다.
     *
     * @param id 회원정보 식별자
     * @return 회원정보의 인증 토큰
     */
    public String login(Long id) {
        return jwtUtil.encode(id);
    }

    /**
     * 회원정보의 인증 토큰을 확인합니다.
     *
     * @param accessToken 회원정보의 인증 토큰
     * @return 인증 확인을 위해 확인된 회원정보 식별자
     */
    public Long parseToken(String accessToken) {
        if(accessToken == null || accessToken.isBlank()) {
            throw new InvalidAccessTokenException(accessToken);
        }

        try {
            Claims claims = jwtUtil.decode(accessToken);
            return claims.get("userId", Long.class);
        } catch(SignatureException e) {
            throw new InvalidAccessTokenException(accessToken);
        }
    }
}
