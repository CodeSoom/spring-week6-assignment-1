package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.dto.SessionTokenData;
import com.codesoom.assignment.errors.WrongPasswordException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.security.SignatureException;
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
     * 자원에 접근할 수 있는 권한을 부여하는 토큰을 발급하고 반환합니다.
     *
     * @param loginData 인증을 위한 로그인 정보
     * @return 자원에 접근할 수 있는 권한을 부여하는 토큰
     */
    public String login(LoginData loginData) {
        User user = this.userService.getUser(loginData.getEmail());
        if (user.getPassword().equals(loginData.getPassword())) {
            return this.jwtUtil.encode(user.getId());
        }
        throw new WrongPasswordException();
    }


    public SessionTokenData verify(String token) {
        return this.jwtUtil.decode(token);
    }
}
