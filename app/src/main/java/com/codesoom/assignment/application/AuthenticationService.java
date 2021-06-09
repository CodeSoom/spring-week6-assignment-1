package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.errors.WrongPasswordException;
import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Service;

/**
 * 자원에 접근할 수 있는 권한을 관리합니다.
 */
@Service
public class AuthenticationService {

    private final JwtUtil jwtUtil;

    private final UserService userService;

    public AuthenticationService(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    /**
     * 자원에 접근할 수 있는 권한을 발급하고 반환합니다.
     *
     * @param email 인증을 요청한 이메일
     * @param password 인증을 요청한 비밀번호
     * @return 자원에 접근할 수 있는 권한을 부여하는 토큰
     */
    public String login(String email, String password) {
        User user = this.userService.getUser(email);
        if (user.getPassword().equals(password)) {
            return this.jwtUtil.encode(user.getId());
        }
        throw new WrongPasswordException();
    }
}
