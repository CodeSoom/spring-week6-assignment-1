package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.errors.WrongPasswordException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

/**
 * 회원 인증 service 입니다.
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
     * email을 받아서 user를 찾아 존재하는 user 라면 encode 하여 jwt 토큰을 리턴합니다.
     *
     * @param email    로그인을 요청한 유저의 email
     * @param password 로그인을 요청한 유저의 password
     * @return jwt 토큰
     * @throws WrongPasswordException 기존에 존재하는 user 의 비밀번호가 요청받은 비밀번호와 다를 경우
     */
    public String login(String email, String password) {
        User user = userService.findUserByEmail(email);

        if (!user.getPassword().equals(password)) {
            throw new WrongPasswordException();
        }

        return jwtUtil.encode(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    /**
     * accessToken 을 받아 분해한 결과를 리턴합니다.
     *
     * @param accessToken 분해 할 token 입니다.
     * @return 분해 된 토큰의 정보 입니다.
     */
    public Long parseToken(String accessToken) {
        Claims claims = jwtUtil.decode(accessToken);
        return claims.get("userId", Long.class);
    }
}
