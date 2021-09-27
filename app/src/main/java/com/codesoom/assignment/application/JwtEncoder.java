package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.AccessToken;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtEncoder {

    private final Key key;

    public JwtEncoder(@Value("#{environment['jwt.secret']}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 암호화한 액세스 토큰을 리턴합니다.
     *
     * @return 액세스 토큰
     */
    public AccessToken encode(User user) {
        return new AccessToken(Jwts.builder()
            .claim("userId", user.getId())
            .signWith(key)
            .compact());
    }
}
