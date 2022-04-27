package com.codesoom.assignment.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
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

    private final Key key;

    public JsonWebTokenManager(@Value("${jwt.secret-key}") String secretKey) {
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * JWT 의 고유 아이디를 리턴합니다.
     *
     * @param token JWT
     */
    public Long getJwtId(String token) {
        return null;
    }

    /**
     * JWT 를 생성합니다.
     *
     * @param attribute 생성시 필요 데이터
     * @return JWT
     */
    public String createToken(JsonWebTokenAttribute attribute) {
        return Jwts.builder()
                .signWith(key)
                .setHeader(makeHeaders())
                .setClaims(makeClaims(attribute.getId()))
                .setExpiration(makeExpiration(attribute.getExpireMinute()))
                .compact();
    }

    /**
     * JWT 헤더 정보를 리턴합니다.
     *
     * @return JWT 헤더
     */
    private Map<String, Object> makeHeaders() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", SignatureAlgorithm.HS256);
        return headers;
    }

    /**
     * JWT Payload 에 들어갈 클레임 리스트를 리턴합니다.
     *
     * @param id 인증된 회원 고유 아이디
     * @return 클레임 리스트
     */
    private Map<String, Object> makeClaims(final Long id) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        return claims;
    }

    /**
     * JWT 만료 시간을 리턴합니다.
     *
     * @param expireMinute 만료할 시간 (분)
     * @return 만료 시간
     */
    private Date makeExpiration(Integer expireMinute) {
        if (expireMinute == null) {
            return null;
        }

        long expiredMiliSecond = 1000 * 60L * expireMinute;

        Date exp = new Date();
        exp.setTime(exp.getTime() + expiredMiliSecond);
        return exp;
    }
}
