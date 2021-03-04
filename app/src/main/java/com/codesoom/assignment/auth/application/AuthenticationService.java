package com.codesoom.assignment.auth.application;

import com.codesoom.assignment.auth.infra.JwtTokenProvider;
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
     * 올바른 사용자 정보라면 토큰 문자열을 리턴하고, 그렇지 않다면 예외를 던집니다.
     *
     * @param email 사용자 이메일
     * @param password 사용자 비밀번호
     * @return 인증 토큰
     * @throws IllegalArgumentException 유효하지 않은 인자가 들어왔을 경우
     */
    public String authenticate(String email, String password) throws IllegalArgumentException {
        final User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(email));

        if (!user.authenticate(password)) {
            throw new IllegalArgumentException(WRONG_PASSWORD);
        }
        return createToken(user.getId());
    }

    /**
     * 사용자 아이디에 해당하는 인증 토큰을 리턴합니다.
     *
     * @param userId 사용자 id
     * @return 생성된 인증 토큰
     */
    private String createToken(Long userId) {
        return jwtTokenProvider.createToken(userId);
    }

    /**
     * 토큰을 파싱하여 인증 정보를 리턴합니다.
     *
     * @param token 인증 토큰
     * @return 인증 정보
     */
    public Claims parseToken(String token) {
        return jwtTokenProvider.decode(token);
    }
}
