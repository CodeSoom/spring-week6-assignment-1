package com.codesoom.assignment.application;

import com.codesoom.assignment.errors.UnauthorizedException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;

/**
 * 유저 인증 로직 담당.
 */
@Service
public class AuthenticationService {

    private final JwtUtil jwtUtil;

    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    /**
     * 유저 id로 인코딩한 토큰 return.
     * @param userId
     * @return 인증 토큰
     */
    public String login(Long userId) {
        return jwtUtil.encode(userId);
    }


    public Long parsetoken(String accessToken) {
        if (accessToken == null || accessToken.isBlank()) {
            throw new UnauthorizedException(accessToken);
        }
        try {
            Claims claims = jwtUtil.decode(accessToken);
            return claims.get("userId", Long.class);
        } catch (SignatureException e) {
            throw new UnauthorizedException(accessToken);
        }
    }
}
