package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.errors.InvalidPasswordException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.infra.JpaUserRepository;
import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Service;

/**
 *  인증에 관련된 로그인 등의 작업을 수행한다
 */
@Service
public class AuthenticationService {
    private JwtUtil jwtUtil;
    private JpaUserRepository userRepository;

    public AuthenticationService(JwtUtil jwtUtil, JpaUserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    /**
     * 이메일과 비밀번호가 일치하는지 인증하여 토큰을 발급한다
     */
    public String login(SessionRequestData sessionRequestData) {
        User user = userRepository.findByEmail(sessionRequestData.getEmail())
                .orElseThrow(() -> new UserNotFoundException(sessionRequestData.getEmail()));
        if(!user.authenticate(sessionRequestData.getPassword())) throw new InvalidPasswordException();
        return jwtUtil.encode(user.getId());
    }
}
