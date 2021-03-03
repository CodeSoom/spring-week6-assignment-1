package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserLoginDetail;
import com.codesoom.assignment.errors.AuthenticationFailException;
import com.codesoom.assignment.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 사용자 인증 서비스
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    /**
     * 사용자의 로그인 정보가 들어오면 유효한 사용자인 경우
     * 토큰을 리턴한다
     * @param userLoginDetail 사용자 정보를 가지고 있는 객체
     * @return Token 유효한 토큰
     */
    public String login(UserLoginDetail userLoginDetail) {

        String email = userLoginDetail.getEmail();
        User user = userService.findUserByEmail(email);

        user.authenticate(userLoginDetail.getPassword());

        return jwtUtil.encode(user.getId());
    }
}
