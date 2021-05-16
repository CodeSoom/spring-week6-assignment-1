package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserLoginDetail;
import com.codesoom.assignment.errors.AuthenticationFailException;
import com.codesoom.assignment.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 사용자 인증 관련 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    /**
     * 로그인 정보를 받아, 유효한 사용자라면 토큰 문자열을 리턴한다.
     * 토큰을 리턴한다
     * @param userLoginDetail 사용자 정보
     * @return 토큰 문자열
     */
    public String login(UserLoginDetail userLoginDetail) {

        String email = userLoginDetail.getEmail();
        User user = userService.findUserByEmail(email);

        user.authenticate(userLoginDetail.getPassword());

        return jwtUtil.encode(user.getId());
    }
}
