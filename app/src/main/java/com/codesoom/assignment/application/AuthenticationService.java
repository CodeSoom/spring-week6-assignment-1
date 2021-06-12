package com.codesoom.assignment.application;

import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtUtil jwtUtil;

    /**
     * 사용자 식별자로 JWT를 발급해서 리턴합니다.
     *
     * @return Jwt String
     */
    public String login() {
        return jwtUtil.customEncode(1L);
    }

    public Long parseToken(String access_token) {
        if(access_token == null || access_token.isBlank()){
            throw new InvalidAccessTokenException(access_token);
        }

        try {
            Claims claims = jwtUtil.customDecode(access_token);
            return claims.get("userId",Long.class);
        }catch (SignatureException e){
            throw new InvalidAccessTokenException(access_token);
        }

    }
}
