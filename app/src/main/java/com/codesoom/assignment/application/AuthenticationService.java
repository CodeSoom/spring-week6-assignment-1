package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.UserAuthenticationFailedException;
import com.codesoom.assignment.errors.UserEmailNotExistException;
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
     * 주어진 유저 id를 부호화하여 토큰을 반환합니다.
     *
     * @param userId 유저 id
     * @return 토큰
     */
    public String encode(Long userId) {
        return jwtUtil.encode(userId);
    }

    /**
     * 주어진 토큰을 해독하여 유저 id를 반환합니다.
     *
     * @param token 토큰
     * @return 유저 id
     */
    public Long decode(String token) {
        return jwtUtil.decode(token);
    }

    /**
     * 주어진 유저 정보로 유저 인증 후 인증 토큰을 반환합니다.
     *
     * @param email    유저 email
     * @param password 유저 password
     * @return 인증 토큰
     * @throws UserAuthenticationFailedException 인증에 실패한 경우
     */
    public String createSession(String email, String password) {
        User user = findUserByEmail(email);
        if (!user.authenticate(password)) {
            throw new UserAuthenticationFailedException(email);
        }

        return encode(user.getId());
    }

    /**
     * 주어진 유저 email와 일치하는 유저를 반환합니다.
     *
     * @param email 유저 email
     * @return 주어진 email을 가지고 있는 유저
     */
    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserEmailNotExistException(email));
    }
}
