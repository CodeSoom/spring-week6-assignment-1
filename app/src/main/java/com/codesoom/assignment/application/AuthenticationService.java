package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.AuthenticationCreateData;
import com.codesoom.assignment.dto.AuthenticationResultData;
import com.codesoom.assignment.dto.SessionResultData;
import com.codesoom.assignment.dto.UserResultData;
import com.codesoom.assignment.errors.AuthenticationBadRequestException;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/** 사용자 인증에 대한 요청을 수행한다. */
@Service
@Transactional
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthenticationService(
            JwtUtil jwtUtil,
            UserRepository userRepository
    ) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    /**
     * 주어진 이메일과 비밀번호에 해당하는 사용자를 리턴한다.
     *
     * @param email - 조회하고자 하는 사용자 이메일
     * @param password - 조회하고자 하는 사용자 비밀번호
     * @return 주어진 {@code email}에 해당하는 사용자
     * @throws AuthenticationBadRequestException 만약
     *         {@code email}에 해당되는 사용자가 저장되어 있지 않은 경우
     *         {@code email}에 해당하는 사용자가 저장되어 있지만 {@code password}이 다른 경우
     *         {@code email}에 해당하는 사용자가 저장되어 있지만  이미 삭제된 경우
     */
    public UserResultData authenticateUser(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(u -> u.authenticate(password))
                .map(UserResultData::of)
                .orElseThrow(AuthenticationBadRequestException::new);
    }

    /**
     * 주어진 사용자를 인증하고 사용자의 이메일을 이용해 토큰을 생성하여 리턴한다.
     *
     * @param authenticationCreateData - 인증하고자 하는 사용자
     * @return 생성된 토큰
     */
    public SessionResultData createToken(AuthenticationCreateData authenticationCreateData) {
        UserResultData userResultData = authenticateUser(
                authenticationCreateData.getEmail(),
                authenticationCreateData.getPassword());

        String accessToken = jwtUtil.encode(userResultData.getEmail());

        return SessionResultData.from(accessToken);
    }

    /**
     * 주어진 토큰을 해석하여 사용자 정보를 리턴한다.
     *
     * @param token - 파싱하고자 하는 토큰 문자열
     * @return 주어진 {@code accessToken}의 사용자 정보
     * @throws InvalidTokenException 만약
     *         {@code token}이 비어있는 경우, 공백인 경우, 서명이 실패한 경우
     */
    public AuthenticationResultData parseToken(String token) {
        if(token == null || token.isBlank()) {
            throw new InvalidTokenException(token);
        }

        try {
            Claims claims = jwtUtil.decode(token);
            return AuthenticationResultData.of(claims);
        } catch(SignatureException e) {
            throw new InvalidTokenException(token);
        }
    }
}
