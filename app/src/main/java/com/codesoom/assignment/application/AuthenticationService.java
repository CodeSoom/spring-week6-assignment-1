package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.AccessToken;
import com.codesoom.assignment.errors.UserNotAuthenticatedException;
import com.codesoom.assignment.errors.UserNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 인증 관련 처리를 담당합니다.
 */
@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtEncoder jwtEncoder;

    public AuthenticationService(UserRepository userRepository,
        JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * 회원을 인증하고 액세스 토큰을 생성해 리턴합니다.
     *
     * @param loginForm 인증할 회원 정보
     * @return 생성된 액세스 토큰
     * @throws UserNotFoundException         회원을 찾지 못한 경우
     * @throws UserNotAuthenticatedException 삭제되었거나 인증에 실패한 경우
     */
    public AccessToken authenticate(LoginForm loginForm) {
        String email = loginForm.getEmail();
        String password = loginForm.getPassword();

        User findUser = findUserByEmail(email);
        if (!findUser.authenticate(password)) {
            throw new UserNotAuthenticatedException(email);
        }

        return jwtEncoder.encode(findUser);
    }

    /**
     * 이메일로 회원을 찾아 리턴합니다.
     *
     * @param email 찾을 회원 이메일
     * @return 찾은 회원
     * @throws UserNotFoundException 회원을 찾지 못한 경우
     */
    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException(email));
    }
}
