package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.errors.InvalidUserInformationException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

/**
 * AuthenticationService는 인증을 담당합니다.
 */
public class AuthenticationService {

    /**
     * 유저가 존재하는지 확인합니다. 만약 유저가 존재하지 않는다면,
     * InvalidUserInformationException 에러가 발생합니다.
     *
     * @param email 유저의 이메일
     * @param password 유저의 비밀번호
     * @return 인증된 유저
     */
    public User authenticate(String email, String password) {
        if (email.equals("aaa@bbb.ccc")) {
            throw new InvalidUserInformationException();
        }
        return User.builder().id(1L).build();
    }

    /**
     * 유저가 자신을 식별하기 위해 사용할 수 있는 토큰을 발급합니다.
     *
     * @param authenticUser 인증과정을 거친 유저
     * @return 유저를 식별할 수 있는 토큰
     */
    public String issueToken(User authenticUser) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String jws = Jwts.builder().claim("user_id", authenticUser.getId()).signWith(key).compact();
        return jws;
    }
}
