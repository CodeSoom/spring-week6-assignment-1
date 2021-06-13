package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

/**
 * 사용자의 인증과 관련한 로직을 수행합니다.
 */
@Service
public class AuthenticationService {

    private final JwtUtil jwtUtil;
    private  final UserRepository userRepository;

    public AuthenticationService(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    /**
     * 사용자 정보로 로그인하여 인증 토큰을 리턴합니다.
     * @param email 로그인 이메일
     * @param password 로그인 비밀번호
     * @return 인증 토큰
     */
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new LoginFailException(email));

        if (!user.authenticate(password)) {
            throw new LoginFailException(email);
        }
        return jwtUtil.encode(user.getId());
    }

    /**
     * 주어진 엑세스 토큰에서 사용자 아이디를 복호화해 리턴합니다.
     * @param accessToken 로그인하려는 사용자의 엑세스 토큰
     * @return 사용자 ID
     */
    public Long parseToken(String accessToken) {
            Claims claims = jwtUtil.decode(accessToken);
            return claims.get("userId", Long.class);
    }
}
