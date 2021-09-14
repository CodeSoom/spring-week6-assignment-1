package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.AccessToken;
import com.codesoom.assignment.dto.LoginRequestDto;
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

    public AuthenticationService(Mapper dozerMapper,
        UserRepository userRepository) {
        this.mapper = dozerMapper;
        this.userRepository = userRepository;
    }

    /**
     * 회원을 인증하고 JWT를 생성해 리턴합니다.
     *
     * @param loginRequestDto 인증할 회원 정보
     * @return 생성된 JWT
     */
    public AccessToken authenticate(LoginRequestDto loginRequestDto) {
        // TODO: 회원 인증, JWT 생성 과정 필요
        User user = mapper.map(loginRequestDto, User.class);
        User findUser = findUserFromEmail(user);

        if (!findUser.authenticate(user.getPassword())) {
            throw new RuntimeException("");
        }

        return new AccessToken("");
    }

    private User findUserFromEmail(User user) {
        return userRepository.findByEmail(user.getEmail())
            .orElseThrow(() -> new UserNotFoundException(user.getId()));
    }
}
