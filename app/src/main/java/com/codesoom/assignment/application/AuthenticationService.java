package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Identifier;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.LoginDataNotMatchedException;
import com.codesoom.assignment.errors.UserNotFoundException;
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

    /**
     * 사용자를 식별하여 인증하고, 인증 토큰을 발급한다.
     *
     * @param identifier 사용자 식별 정보
     * @return 인증 토큰
     * @throws LoginDataNotMatchedException 인증에 실패한 경우
     */
    public String login(Identifier identifier) {

        final User foundUser = userRepository.findByEmailAndDeletedIsFalse(identifier)
                .orElseThrow(() -> new UserNotFoundException(identifier.getEmail()));

        if (!foundUser.authenticate(identifier.getPassword())) {
            throw new LoginDataNotMatchedException();
        }

        return jwtUtil.encode(foundUser.getId());
    }
}
