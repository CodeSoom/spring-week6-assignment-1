package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.AccessToken;
import com.codesoom.assignment.dto.LoginRequestDto;
import com.codesoom.assignment.errors.UserNotAuthenticatedException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

/**
 * 인증 관련 처리를 담당합니다.
 */
@Service
public class AuthenticationService {

    private final Mapper mapper;
    private final UserRepository userRepository;

    public AuthenticationService(Mapper dozerMapper, UserRepository userRepository) {
        this.mapper = dozerMapper;
        this.userRepository = userRepository;
    }

    /**
     * 회원을 인증하고 액세스 토큰을 생성해 리턴합니다.
     *
     * @param loginRequestDto 인증할 회원 정보
     * @return 생성된 액세스 토큰
     */
    public AccessToken authenticate(LoginRequestDto loginRequestDto) {
        User user = mapper.map(loginRequestDto, User.class);
        User findUser = findUserFromEmail(user);

        if (!findUser.authenticate(user.getPassword())) {
            throw new UserNotAuthenticatedException(user);
        }

        // TODO: JWT 생성 과정 필요

        String secret = "12345678901234567890123456789012";
        return new JwtEncoder(secret).encode();
    }

    private User findUserFromEmail(User user) {
        return userRepository.findByEmail(user.getEmail())
            .orElseThrow(() -> new UserNotFoundException(user.getId()));
    }
}
