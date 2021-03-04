package com.codesoom.assignment.util;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.UserBadRequestException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {
    private final Key key;
    private final UserRepository userRepository;

    public JwtUtil(@Value("${jwt.secret}") String secret
            , UserRepository userRepository
    ) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
        this.userRepository = userRepository;
    }

    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(UserBadRequestException::new);
    }

    public String encode(String email, String password) {
        User user = getUser(email);

        if(!user.authenticate(password)) {
            throw new UserBadRequestException();
        }

        return Jwts.builder()
                .claim("email", email)
                .claim("password", password)
                .signWith(key)
                .compact();
    }

    public Claims decode(String token) {
        if(token == null || token.isBlank()) {
            throw new InvalidTokenException(token);
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch(SignatureException e) {
            throw new InvalidTokenException(token);
        }

    }
}
