package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.dto.ProductNotFoundException;
import com.codesoom.assignment.errors.UnauthorizedException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;

@Service
public class AuthenticationService {

    private JwtUtil jwtUtil;

    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String login() {

        return jwtUtil.encode(1L);

    }

    public Long parseToken(String accessToken) {

        if(accessToken==null || accessToken.isBlank()) {
            throw new UnauthorizedException();
        }

        try {
            Claims decode = jwtUtil.decode(accessToken);
            Long userId = decode.get("userId", Long.class);
            return userId;
        }catch (SignatureException e) {
            throw new UnauthorizedException();
        }

     }

}
