package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.AccessToken;
import org.springframework.stereotype.Service;

/**
 * 인증 관련 처리를 담당합니다.
 */
@Service
public class AuthenticationService {

    /**
     * 회원을 인증하고 JWT를 생성해 리턴합니다.
     *
     * @param user 인증할 회원
     * @return 생성된 JWT
     */
    public AccessToken authenticate(User user) {
        // TODO: 회원 인증, JWT 생성 과정 필요
        return null;
    }
}
