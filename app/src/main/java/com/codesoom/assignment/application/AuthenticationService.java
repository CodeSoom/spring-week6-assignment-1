package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.PasswordNotCorrectException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Service;

/**
 * 인증 관련 비즈니스 로직을 처리합니다.
 */
@Service
public class AuthenticationService {

    private JwtUtil jwtUtil;

    private UserRepository userRepository;

    public AuthenticationService(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    /**
     * 사용자 로그인 정보로 로그인하고 토큰을 발행해 리턴합니다.
     * @param userLoginData 로그인 하기 위한 사용자 정보
     * @return 발행 한 토큰
     */
    public String login(UserLoginData userLoginData) {
        User foundUser = userRepository.findByEmail(userLoginData.getEmail())
                                       .orElseThrow(() -> new UserNotFoundException(userLoginData.getEmail()));

        if (!foundUser.authenticate(userLoginData.getPassword())) {
            throw new PasswordNotCorrectException();
        }

        return jwtUtil.encode(userLoginData.getEmail());
    }

    /**
     * 엑세스 토큰을 디코드해서 사용자 이메일을 리턴합니다.
     * @param accessToken 엑세스 토큰
     * @return 사용자 이메일
     */
    public String parseToken(String accessToken) {
        return jwtUtil.decode(accessToken).get("userEmail", String.class);
    }
}
