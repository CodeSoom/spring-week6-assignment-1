package com.codesoom.assignment.auth.application;

import com.codesoom.assignment.auth.dto.LoginRequest;
import com.codesoom.assignment.auth.infra.JwtTokenProvider;
import com.codesoom.assignment.user.domain.EmailSupplier;
import com.codesoom.assignment.user.domain.User;
import com.codesoom.assignment.user.domain.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 인증을 처리한다.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {
    private static final String WRONG_DATA = "잘못된 정보를 입력하였습니다.";
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    /**
     * 올바른 사용자 정보라면 토큰 문자열을 리턴하고, 그렇지 않다면 예외를 던집니다.
     *
     * @param requestDto 로그인 인증 요청
     * @return 인증 토큰
     * @throws IllegalArgumentException 유효하지 않은 인자가 들어왔을 경우
     */
    public String authenticate(LoginRequest requestDto) throws IllegalArgumentException {
        final User user = findUserByEmail(requestDto);

        if (!user.authenticate(requestDto.getPassword())) {
            throw new IllegalArgumentException(WRONG_DATA);
        }
        return createToken(user.getId());
    }

    private User findUserByEmail(EmailSupplier emailSupplier) {
        return userRepository.findByEmail(emailSupplier.getEmail())
                .orElseThrow(() -> new IllegalArgumentException(WRONG_DATA));
    }

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
