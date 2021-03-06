package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginDataGettable;
import com.codesoom.assignment.errors.UserAuthenticationFailedException;
import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Service;

/**
 * 유저 인증에 대한 로직을 담당.
 */
@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthenticationService(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    /**
     * 주어진 유저 id를 부호화한 인증 토큰을 반환합니다.
     *
     * @param userId 유저 id
     * @return 인증 토큰
     */
    public String encode(Long userId) {
        return jwtUtil.encode(userId);
    }

    /**
     * 주어진 토큰을 해독한 유저 id를 반환합니다.
     *
     * @param token 토큰
     * @return 유저 id
     */
    public Long decode(String token) {
        return jwtUtil.decode(token);
    }

    /**
     * 주어진 로그인 정보로 생성한 인증 토큰을 반환합니다.
     *
     * @param loginData 로그인 정보
     * @return 인증 토큰
     * @throws UserAuthenticationFailedException 인증에 실패한 경우
     */
    public String createSession(UserLoginDataGettable loginData) {
        User user = findUserByEmail(loginData);
        if (!user.authenticate(loginData)) {
            throw new UserAuthenticationFailedException(loginData);
        }

        return encode(user.getId());
    }

    /**
     * 주어진 로그인 정보와 일치하는 유저를 반환합니다.
     *
     * @param loginData 로그인 정보
     * @return 유저
     */
    private User findUserByEmail(UserLoginDataGettable loginData) {
        return userRepository.findByEmail(loginData.getEmail())
                .orElseThrow(() -> new UserAuthenticationFailedException(loginData));
    }
}
