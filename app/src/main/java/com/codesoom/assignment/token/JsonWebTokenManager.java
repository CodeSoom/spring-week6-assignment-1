package com.codesoom.assignment.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 생성, 검증 담당
 */
@Component
public class JsonWebTokenManager {

    private static final String SECRET_KEY = "12345678901234567890123456789012";

    private final Key key;

    public JsonWebTokenManager() {
        key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
     * JWT 를 생성합니다.
     * @param attribute 생성시 필요 데이터
     * @return Json Web Token
     */
    public String createToken(JsonWebTokenAttribute attribute) {
        return Jwts.builder()
                .signWith(key)
                .setHeader(makeHeaders())
                .setClaims(makeClaims(attribute))
                .setExpiration(makeExpiration(attribute))
                .compact();
    }

    private Map<String, Object> makeHeaders() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", SignatureAlgorithm.HS256);
        return headers;
    }

    private Date makeExpiration(JsonWebTokenAttribute attribute) {
        if (attribute.getExpireMinute() == null) {
            return null;
        }

        long expiredMiliSecond = 1000 * 60L * attribute.getExpireMinute();
        Date exp = new Date();
        exp.setTime(exp.getTime() + expiredMiliSecond);
        return exp;
    }

    private Map<String, Object> makeClaims(final JsonWebTokenAttribute attribute) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", attribute.getId());
        return claims;
    }
}
