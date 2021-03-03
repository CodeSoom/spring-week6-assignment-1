package com.codesoom.assignment.auth.application;

import com.codesoom.assignment.auth.infra.JwtTokenProvider;
import com.codesoom.assignment.user.application.UserEmailNotFoundException;
import com.codesoom.assignment.user.domain.User;
import com.codesoom.assignment.user.domain.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 사용자 인증을 처리한다.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final String WRONG_PASSWORD = "잘못된 비밀번호를 입력하였습니다.";
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    /**
     * 올바른 사용자인지 인증한다.
     *
     * @param email    입력받은 이메일
     * @param password 입력받은 비밀번호
     * @return 인증 토큰
     */
    public String authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException(email));

        if (!user.matchPassword(password)) {
            throw new IllegalArgumentException(WRONG_PASSWORD);
        }
        return createToken(user.getId());
    }

    /**
     * 인증 토큰을 생성한다.
     *
     * @param userId 사용자 id
     * @return 인증 토큰
     */
    private String createToken(Long userId) {
        return jwtTokenProvider.createToken(userId);
    }

    /**
     * 토큰을 풀어 토큰 정보를 확인합니다.
     *
     * @param token 인증된 토큰
     * @return 토큰 정보
     */
    public Claims parseToken(String token) {
        return jwtTokenProvider.decode(token);
    }
}
