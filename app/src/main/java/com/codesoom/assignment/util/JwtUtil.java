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

/** 토큰에 대해 처리한다 */
@Component
public class JwtUtil {
    private final Key key;
    private final UserRepository userRepository;

    public JwtUtil(@Value("${jwt.secret}") String secret
            ,UserRepository userRepository
    ) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
        this.userRepository = userRepository;
    }

    /**
     * 주어진 이메일에 해당하는 사용자를 리턴한다.
     *
     * @param email - 조회하고자 하는 사용자 이메일
     * @return 주어진 {@code email}에 해당하는 사용자
     * @throws UserBadRequestException 만약
     *         {@code email}에 해당되는 사용자가 저장되어 있지 않은 경우
     */
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(UserBadRequestException::new);
    }

    /**
     * 주어진 이메일과 비밀번호로 토큰 문자열을 생성하고 리턴한다.
     *
     * @param email - 토큰 문자열 생성을 위한 이메일
     * @param password - 토큰 문자열 생성을 위한 비밀번호
     * @return - 주어진 {@code email}과 {@code password}를 이용하여 생성된 토큰 문자열
     * @throws UserBadRequestException 만약
     *         {@code password}에 해당하는 사용자가 저장되어 있지 않은 경우
     *         ,이미 삭제되어 있는 경우
     */
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

    /**
     * 주어진 토큰 문자열을 파싱하여 사용자 정보를 리턴한다.
     *
     * @param token - 파싱하고자 하는 토큰 문자열
     * @return 주어진 {@code token}의 사용자 정보
     * @throws InvalidTokenException 만약
     *         {@code token}이 비어있는 경우, 공백인 경우, 서명이 실패한 경우
     */
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
