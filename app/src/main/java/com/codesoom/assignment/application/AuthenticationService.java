package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.LoginNotMatchPasswordException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Service;

/**
 * 인증, 인가와 관련된 처리를 담당합니다.
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
     * User 로그인 정보를 확인해 access token 발급합니다.
     *
     * @param userLoginData email과 password를 담은 User 정보
     * @return access token
     * @throws UserNotFoundException User 정보가 존재하지 않는 경우 던집니다.
     * @throws LoginNotMatchPasswordException User의 password가 일치하지 않는 경우 던집니다.
     */
    public String login(UserLoginData userLoginData) {
        User findedUser = userRepository.findByEmail(userLoginData.getEmail())
                .orElseThrow(UserNotFoundException::new);

        if(!findedUser.authenticate(userLoginData.getPassword())) {
            throw new LoginNotMatchPasswordException(findedUser.getEmail());
        }

        return jwtUtil.encode(findedUser.getId());
    }

    /**
     * access token가 유효한지 확인합니다.
     *
     * @param token 발행된 access token
     * @throws InvalidAccessTokenException access token이 유효하지 않는 경우 던집니다.
     */
    public void checkToken(String token) {
        jwtUtil.decode(token);
    }
}
