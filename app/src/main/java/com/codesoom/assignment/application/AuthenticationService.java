package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 회원 인증 로직을 처리합니다.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtUtil jwtUtil;

    /**
     * 회원의 access token을 반환합니다.
     *
     * @param user 회원
     * @return JWT Access Token
     */
    public String login(User user) {
        return jwtUtil.encode(user.getId());
    }
}
