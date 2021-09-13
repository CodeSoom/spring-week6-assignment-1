package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.LoginForm;
import com.codesoom.assignment.errors.LoginDataNotMatchedException;
import com.codesoom.assignment.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 인증 token 정보를 관리한다.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public String login(LoginForm form) {

        final User foundUser = userRepository.findByEmailAndDeletedIsFalse(form.getEmail())
                .orElseThrow(LoginDataNotMatchedException::new);

        if (!foundUser.getPassword().equals(form.getPassword())) {
            throw new LoginDataNotMatchedException();
        }

        return jwtUtil.encode(foundUser.getId());
    }
}
