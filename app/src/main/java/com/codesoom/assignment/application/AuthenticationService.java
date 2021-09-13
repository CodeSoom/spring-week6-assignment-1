package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.LoginForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 인증 token 정보를 관리한다.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    public String login(LoginForm form) {
        return null;
    }
}
