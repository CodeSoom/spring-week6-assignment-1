package com.codesoom.assignment.application;

import com.codesoom.assignment.dto.AuthenticationCreateData;
import com.codesoom.assignment.dto.AuthenticationResultData;
import com.codesoom.assignment.dto.SessionResultData;
import com.codesoom.assignment.dto.UserResultData;
import com.codesoom.assignment.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/** 사용자 인증에 대한 요청을 수행한다. */
@Service
@Transactional
public class AuthenticationService {
    private final JwtUtil jwtUtil;

    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 주어진 사용자를 인증하고 사용자의 이메일을 이용해 토큰을 생성하여 리턴한다.
     *
     * @param authenticationCreateData - 인증하고자 하는 사용자
     * @return 생성된 토큰
     */
    public SessionResultData createToken(AuthenticationCreateData authenticationCreateData) {
        UserResultData userResultData = jwtUtil.authenticateUser(
                authenticationCreateData.getEmail(),
                authenticationCreateData.getPassword());

        String accessToken = jwtUtil.encode(userResultData.getEmail());

        return SessionResultData.from(accessToken);
    }

    /**
     * 주어진 토큰을 해석하여 사용자 정보를 리넌한다.
     *
     * @param accessToken - 파싱하고자 하는 토큰 문자열
     * @return 주어진 {@code accessToken}의 사용자 정보
     */
    public AuthenticationResultData parseToken(String accessToken) {
        Claims claims = jwtUtil.decode(accessToken);

        return AuthenticationResultData.of(claims);
    }
}
